package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.Category;
import com.karmperis.expensetracker.model.CategoryType;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing category entities.
 */
public interface CategoryRepository extends JpaRepository <Category, Long> {
    /**
     * Retrieves a category based on UUID and user.
     * @param uuid The UUID of the category to search for.
     * @param user The user who owns the category.
     * @return The found category, or empty if no matching category is found.
     */
    Optional<Category> findByUuidAndUser(UUID uuid, User user);

    /**
     * Retrieves all active categories associated with a specific user.
     * @param user The user entity to filter categories by.
     * @return A list of active categories belonging to the specified user.
     */
    List<Category> findByUserAndActiveTrueOrderByCategoryAsc(User user);

    /**
     * Retrieves all inactive categories associated with a specific user.
     * @param user The user entity to filter categories by.
     * @return A list of inactive categories belonging to the specified user.
     */
    List<Category> findByUserAndActiveFalseOrderByCategoryAsc(User user);

    /**
     * Retrieves active categories for a specific user filtered by category type.
     * @param user The user entity.
     * @param categoryType The type of category to filter by.
     * @return A list of active categories matching the type.
     */
    List<Category> findByUserAndCategoryTypeAndActiveTrueOrderByCategoryAsc(User user, CategoryType categoryType);

    /**
     * Retrieves inactive categories for a specific user filtered by category type.
     * @param user The user entity.
     * @param categoryType The type of category to filter by.
     * @return A list of inactive categories matching the type.
     */
    List<Category> findByUserAndCategoryTypeAndActiveFalseOrderByCategoryAsc(User user, CategoryType categoryType);

    /**
     * Checks if a category with the given name already exists for a specific user.
     * @param user The user entity to check.
     * @param category The name of the category to check.
     * @return True if a matching category exists, false otherwise.
     */
    boolean existsByUserAndCategory(User user, String category);
}