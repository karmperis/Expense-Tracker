package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.Account;
import com.karmperis.expensetracker.model.Category;
import com.karmperis.expensetracker.model.Transaction;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing transaction entities.
 */
public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    /**
     * Retrieves a transaction based on UUID and user.
     * @param uuid The UUID of the category to search for.
     * @param user The user who owns the category.
     * @return The found category, or empty if no matching category is found.
     */
    Optional<Transaction> findByUuidAndUser(UUID uuid, User user);

    /**
     * Retrieves all active transactions associated with a specific user.
     * @param user The user entity to filter transactions by.
     * @return A list of active transactions belonging to the specified user.
     */
    List<Transaction> findByUserAndActiveTrueOrderByDateDesc(User user);

    /**
     * Retrieves active transactions for a specific user within a given date range.
     * @param user The user entity.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of active transactions within the date range.
     */
    List<Transaction> findByUserAndActiveTrueAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves active transactions for a specific user and account.
     * @param user The user entity.
     * @param account The account to filter by.
     * @return A list of active transactions belonging to the specific account.
     */
    List<Transaction> findByUserAndAccountAndActiveTrueOrderByDateDesc(User user, Account account);

    /**
     * Retrieves active transactions for a specific user and category.
     * @param user The user entity.
     * @param category The category to filter by.
     * @return A list of active transactions belonging to the specific category.
     */
    List<Transaction> findByUserAndCategoryAndActiveTrueOrderByDateDesc(User user, Category category);

    /**
     * Retrieves all inactive transactions associated with a specific user.
     * @param user The user entity to filter transactions by.
     * @return A list of inactive transactions belonging to the specified user.
     */
    List<Transaction> findByUserAndActiveFalseOrderByDateDesc(User user);

    /**
     * Retrieves inactive transactions for a specific user within a given date range.
     * @param user The user entity.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of inactive transactions within the date range.
     */
    List<Transaction> findByUserAndActiveFalseAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
}