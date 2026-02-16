package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.Account;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing account entities.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Retrieves an account based on UUID and user.
     * @param uuid The UUID of the account to search for.
     * @return The found account, or empty if no account matches the UUID.
     */
    Optional<Account> findByUuidAndUser(UUID uuid, User user);
    /**
     * Retrieves all accounts associated with a specific user.
     * @param user The user entity to filter accounts by.
     * @return A list of active accounts belonging to the specified user.
     */
    List<Account> findByUserAndActiveTrueOrderByAccountAsc(User user);

    /**
     * Retrieves all accounts associated with a specific user.
     * @param user The user entity to filter accounts by.
     * @return A list of inactive accounts belonging to the specified user.
     */
    List<Account> findByUserAndActiveFalseOrderByAccountAsc(User user);

    /**
     * Checks if an account with the given name already exists for a specific user.
     * @param user The user entity to check.
     * @param account The name of the account to check.
     * @return True if a matching account exists, false otherwise.
     */
    boolean existsByUserAndAccount(User user, String account);
}