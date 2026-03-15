package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
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
     * @return A page of active transactions belonging to the specified user.
     */
    Page<Transaction> findByUserAndActiveTrueOrderByDateDesc(User user, Pageable pageable);

    /**
     * Retrieves active transactions for a specific user within a given date range.
     * @param user The user entity.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A page of active transactions within the date range.
     */
    Page<Transaction> findByUserAndActiveTrueAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Retrieves active transactions for a specific user and account.
     * @param user The user entity.
     * @param account The account to filter by.
     * @return A page of active transactions belonging to the specific account.
     */
    Page<Transaction> findByUserAndAccountAndActiveTrueOrderByDateDesc(User user, Account account, Pageable pageable);

    /**
     * Retrieves active transactions for a specific user and category.
     * @param user The user entity.
     * @param category The category to filter by.
     * @return A page of active transactions belonging to the specific category.
     */
    Page<Transaction> findByUserAndCategoryAndActiveTrueOrderByDateDesc(User user, Category category, Pageable pageable);

    /**
     * Retrieves all inactive transactions associated with a specific user.
     * @param user The user entity to filter transactions by.
     * @return A page of inactive transactions belonging to the specified user.
     */
    Page<Transaction> findByUserAndActiveFalseOrderByDateDesc(User user, Pageable pageable);

    /**
     * Retrieves inactive transactions for a specific user within a given date range.
     * @param user The user entity.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A page of inactive transactions within the date range.
     */
    Page<Transaction> findByUserAndActiveFalseAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Retrieves transactions based on category type.
     * @param user The owner of the transactions.
     * @param categoryType The type of category to filter by.
     * @return A page of active transactions matching the specified category type.
     */
    Page<Transaction> findByUserAndCategory_CategoryTypeAndActiveTrueOrderByDateDesc(
            User user, CategoryType categoryType, Pageable pageable);
}