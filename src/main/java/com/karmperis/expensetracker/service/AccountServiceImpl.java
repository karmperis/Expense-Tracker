package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.AccountEditDTO;
import com.karmperis.expensetracker.dto.AccountInsertDTO;
import com.karmperis.expensetracker.dto.AccountReadOnlyDTO;
import com.karmperis.expensetracker.mapper.AccountMapper;
import com.karmperis.expensetracker.model.Account;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Implementation of the IAccountService for managing accounts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    @Override
    @Transactional(rollbackFor = EntityAlreadyExistsException.class)
    public AccountReadOnlyDTO saveAccount(AccountInsertDTO dto, User user) throws EntityAlreadyExistsException {
        try {
            if (accountRepository.existsByUserAndAccount(user, dto.account())) {
                throw new EntityAlreadyExistsException("Account", "Account with name '" + dto.account() + "' already exists.");
            }

            Account account = mapper.mapToEntity(dto);
            account.setUser(user);

            if (account.getBalance() == null) {
                account.setBalance(BigDecimal.ZERO);
            }

            account.setActive(true);

            accountRepository.save(account);
            log.info("Account '{}' created successfully for user '{}'.", dto.account(), user.getUsername());

            return mapper.mapToReadOnlyDTO(account);
        } catch (EntityAlreadyExistsException e) {
            log.error("Failed to create account: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public AccountReadOnlyDTO updateAccount(UUID uuid, AccountEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        try {
            Account existingAccount = accountRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Account", "Account not found with uuid=" + uuid));

            if (!existingAccount.getAccount().equals(dto.account()) &&
                    accountRepository.existsByUserAndAccount(user, dto.account())) {
                throw new EntityAlreadyExistsException("Account", "Account with name '" + dto.account() + "' already exists.");
            }

            mapper.updateEntityFromEditDTO(existingAccount, dto);

            accountRepository.save(existingAccount);
            log.info("Account with uuid={} updated successfully.", uuid);

            return mapper.mapToReadOnlyDTO(existingAccount);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Update failed for account uuid={}: {}", uuid, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteAccount(UUID uuid, User user) throws EntityNotFoundException {
        try {
            Account account = accountRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Account", "Account not found with uuid=" + uuid));

            account.setActive(false); // Soft delete
            log.info("Account with uuid={} deactivated successfully.", uuid);
        } catch (EntityNotFoundException e) {
            log.error("Deactivation failed for account uuid={}", uuid, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AccountReadOnlyDTO getAccountByUuid(UUID uuid, User user) throws EntityNotFoundException {
        return accountRepository.findByUuidAndUser(uuid, user)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("Account", "Account not found with uuid=" + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountReadOnlyDTO> getAllAccounts(Pageable pageable, User user) {
        return accountRepository.findByUserAndActiveTrueOrderByAccountAsc(user, pageable)
                .map(mapper::mapToReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccountExists(String account, User user) {
        return accountRepository.existsByUserAndAccount(user, account);
    }
}