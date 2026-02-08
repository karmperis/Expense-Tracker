package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.Account;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * Interface for managing Account entities.
 */

public interface AccountRepository extends JpaRepository<Account, UUID> {
    /**
     * Retrieves all accounts associated with a specific user.
     * @param user The User entity to filter accounts by.
     * @return A list of accounts belonging to the specified user.
     */
    List<Account> findByUserOrderByAccountAsc(User user);

    /**
     * Checks if an account with the given name already exists for a specific user.
     * @param user The User entity to check.
     * @param account The name of the account to check.
     * @return True if a matching account exists, false otherwise.
     */
    boolean existsByUserAndAccount(User user, String account);
}