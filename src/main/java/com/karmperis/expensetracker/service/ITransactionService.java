package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.*;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface for transaction service.
 */
public interface ITransactionService {
    /**
     * Creates a new transaction.
     * @param dto Data for the new transaction.
     * @param user The user entity who will own this transaction.
     * @return The created transaction as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If a transaction already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    TransactionReadOnlyDTO saveTransaction(TransactionInsertDTO dto, User user)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Updates an existing transaction.
     * @param uuid The unique identifier of the transaction to update.
     * @param dto The updated data.
     * @param user The user entity who will own this transaction.
     * @return The updated transaction.
     * @throws EntityNotFoundException If a transaction not found.
     * @throws EntityAlreadyExistsException If a transaction already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    TransactionReadOnlyDTO updateTransaction(UUID uuid, TransactionEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a transaction by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this transaction.
     * @throws EntityNotFoundException If a transaction not found.
     */
    void deleteTransaction(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a transaction by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this transaction.
     * @return The transaction details.
     * @throws EntityNotFoundException If a transaction not found.
     */
    TransactionReadOnlyDTO getTransactionByUuid(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all transactions.
     * @param pageable Contains page number, size, and sorting info.
     * @param user The user entity who will own this transaction.
     * @return A page of TransactionReadOnlyDTO.
     */
    Page<TransactionReadOnlyDTO> getAllTransactions(Pageable pageable, User user);
}