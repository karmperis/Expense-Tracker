package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.*;
import com.karmperis.expensetracker.dto.*;
import com.karmperis.expensetracker.mapper.TransactionMapper;
import com.karmperis.expensetracker.model.*;
import com.karmperis.expensetracker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Implementation of the ITransactionService for managing financial transactions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionMapper mapper;

    /**
     * Creates and saves a new transaction.
     * Validates that the associated category, account, and payment method exist and belong to the user.
     * @param dto Data for the new transaction.
     * @param user The user entity who owns this transaction.
     * @return The created transaction as a ReadOnlyDTO.
     * @throws EntityInvalidArgumentException If any associated entity ID is invalid or does not belong to the user.
     */
    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class,
            InvalidAmountException.class, InsufficientFundsException.class})
    public TransactionReadOnlyDTO saveTransaction(TransactionInsertDTO dto, User user)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            validateAmount(dto.amount());

            Transaction transaction = mapper.mapToEntity(dto);

            Category category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Category", "Category not found"));

            Account account = accountRepository.findById(dto.accountId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Account", "Account not found"));

            PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.paymentMethodId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Payment method", "Payment method not found"));

            if (!category.getUser().equals(user) || !account.getUser().equals(user) || !paymentMethod.getUser().equals(user)) {
                throw new EntityInvalidArgumentException("Transaction", "Associated entities must belong to the user.");
            }

            transaction.setCategory(category);
            transaction.setAccount(account);
            transaction.setPaymentMethod(paymentMethod);
            transaction.setUser(user);

            // Update balance logic
            updateBalance(account, category, dto.amount(), true);

            accountRepository.save(account);
            transactionRepository.save(transaction);
            log.info("Transaction for amount {} saved successfully for user {}.", dto.amount(), user.getUsername());

            return mapper.mapToReadOnlyDTO(transaction);

        } catch (EntityInvalidArgumentException | InvalidAmountException | InsufficientFundsException e) {
            log.error("Transaction save failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Updates an existing transaction.
     * @param uuid The unique identifier of the transaction to update.
     * @param dto The updated data.
     * @param user The user entity who owns this transaction.
     * @return The updated transaction details.
     * @throws EntityNotFoundException If the transaction does not exist for this user.
     */
    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class,
            EntityInvalidArgumentException.class, InvalidAmountException.class,
            InsufficientFundsException.class})
    public TransactionReadOnlyDTO updateTransaction(UUID uuid, TransactionEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            validateAmount(dto.amount());

            Transaction existingTransaction = transactionRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Transaction", "Transaction not found"));

            // Revert balance impact from old state
            updateBalance(existingTransaction.getAccount(), existingTransaction.getCategory(), existingTransaction.getAmount(), false);
            accountRepository.save(existingTransaction.getAccount());

            mapper.updateEntityFromEditDTO(existingTransaction, dto);

            if (!existingTransaction.getCategory().getId().equals(dto.categoryId())) {
                Category category = categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Category", "Invalid category ID"));
                existingTransaction.setCategory(category);
            }

            if (!existingTransaction.getAccount().getId().equals(dto.accountId())) {
                Account account = accountRepository.findById(dto.accountId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Account", "Invalid account ID"));
                existingTransaction.setAccount(account);
            }

            if (!existingTransaction.getPaymentMethod().getId().equals(dto.paymentMethodId())) {
                PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.paymentMethodId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Payment method", "Invalid payment method ID"));
                existingTransaction.setPaymentMethod(paymentMethod);
            }

            updateBalance(existingTransaction.getAccount(), existingTransaction.getCategory(), dto.amount(), true);

            accountRepository.save(existingTransaction.getAccount());
            transactionRepository.save(existingTransaction);
            log.info("Transaction with uuid {} updated successfully.", uuid);

            return mapper.mapToReadOnlyDTO(existingTransaction);

        } catch (EntityNotFoundException | EntityInvalidArgumentException | InvalidAmountException | InsufficientFundsException e) {
            log.error("Update failed for transaction uuid {}: {}", uuid, e.getMessage());
            throw e;
        }
    }

    /**
     * Soft deletes a transaction by setting its active status to false.
     * @param uuid The unique identifier of the transaction.
     * @param user The user who owns the transaction.
     * @throws EntityNotFoundException If the transaction is not found.
     */
    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, InsufficientFundsException.class})
    public void deleteTransaction(UUID uuid, User user) throws EntityNotFoundException {
        try {
            Transaction transaction = transactionRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Transaction", "Transaction not found"));

            // Revert balance before deactivating
            updateBalance(transaction.getAccount(), transaction.getCategory(), transaction.getAmount(), false);
            accountRepository.save(transaction.getAccount());

            transaction.setActive(false);
            log.info("Transaction with uuid {} deactivated.", uuid);
        } catch (EntityNotFoundException | InsufficientFundsException e) {
            log.error("Delete failed: Transaction with uuid {} not found.", uuid);
            throw e;
        }
    }

    private void updateBalance(Account account, Category category, BigDecimal amount, boolean isNewAction) {
        boolean isIncome = category.getCategoryType().getType().equalsIgnoreCase("Income");
        BigDecimal adjustment = isIncome ? amount : amount.negate();

        if (!isNewAction) adjustment = adjustment.negate();

        BigDecimal newBalance = account.getBalance().add(adjustment);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Account '" + account.getAccount() + "' cannot have a negative balance.");
        }
        account.setBalance(newBalance);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Transaction amount must be positive.");
        }
    }

    /**
     * Retrieves a single transaction by its UUID.
     * @param uuid The unique identifier of the transaction.
     * @param user The user who owns the transaction.
     * @return The transaction as a ReadOnlyDTO.
     * @throws EntityNotFoundException If the transaction is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionReadOnlyDTO getTransactionByUuid(UUID uuid, User user) throws EntityNotFoundException {
        return transactionRepository.findByUuidAndUser(uuid, user)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("Transaction", "Transaction not found"));
    }

    /**
     * Retrieves a paginated list of all active transactions for a specific user.
     * @param pageable Contains pagination and sorting info.
     * @param user The user entity.
     * @return A page of TransactionReadOnlyDTO.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionReadOnlyDTO> getAllTransactions(Pageable pageable, User user) {
        return transactionRepository.findByUserAndActiveTrueOrderByDateDesc(user, pageable)
                .map(mapper::mapToReadOnlyDTO);
    }
}