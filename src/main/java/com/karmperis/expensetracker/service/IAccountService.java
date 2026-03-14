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
 * Interface for account service.
 */
public interface IAccountService {
    /**
     * Creates a new account.
     * @param dto Data for the new account.
     * @param user The user entity who will own this account.
     * @return The created account as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If an account already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    AccountReadOnlyDTO saveAccount(AccountInsertDTO dto, User user)
        throws EntityAlreadyExistsException, EntityInvalidArgumentException;


    /**
     * Updates an existing account.
     * @param uuid The unique identifier of the account to update.
     * @param dto The updated data.
     * @param user The user entity who will own this account.
     * @return The updated account.
     * @throws EntityNotFoundException If an account not found.
     * @throws EntityAlreadyExistsException If an account already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    AccountReadOnlyDTO updateAccount(UUID uuid, AccountEditDTO dto, User user)
        throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes an account by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this account.
     * @throws EntityNotFoundException If an account not found.
     */
    void deleteAccount(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves an account by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this account.
     * @return The account details.
     * @throws EntityNotFoundException If an account not found.
     */
    AccountReadOnlyDTO getAccountByUuid(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all accounts.
     * @param pageable Contains page number, size, and sorting info.
     * @param user The user entity who will own this account.
     * @return A page of AccountReadOnlyDTO.
     */
    Page<AccountReadOnlyDTO> getAllAccounts(Pageable pageable, User user);

    /**
     * Checks if an account exists for the given user.
     * @param account The name of the account.
     * @param user The user entity who will own this account.
     * @return True if it exists.
     */
    boolean isAccountExists(String account, User user);
}