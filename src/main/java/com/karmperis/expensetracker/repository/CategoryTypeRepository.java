package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.CategoryType;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

    /**
     * Interface for managing category type entities.
     */
    public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {

        /**
         * Retrieves a category type based on UUID and user.
         * @param uuid The UUID of the category type to search for.
         * @param user The user who owns the category type.
         * @return The found category type, or empty if no category type matches the UUID.
         */
        Optional<CategoryType> findByUuidAndUser(UUID uuid, User user);
        /**
         * Retrieves all active category types associated with a specific user.
         * @param user The user entity to filter category types by.
         * @return A list of active category types belonging to the specified user.
         */
        List<CategoryType> findByUserAndActiveTrueOrderByTypeAsc(User user);

        /**
         * Retrieves all inactive category types associated with a specific user.
         * @param user The user entity to filter category types by.
         * @return A list of inactive category types belonging to the specified user.
         */
        List<CategoryType> findByUserAndActiveFalseOrderByTypeAsc(User user);

        /**
         * Checks if a category type with the given name already exists for a specific user.
         * @param user The user entity to check.
         * @param type name of the category type to check.
         * @return True if a matching category type exists, false otherwise.
         */
        boolean existsByUserAndType(User user, String type);
    }