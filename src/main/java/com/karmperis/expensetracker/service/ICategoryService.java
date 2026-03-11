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
 * Interface for category service.
 */
public interface ICategoryService {
    /**
     * Creates a new category.
     * @param dto Data for the new category.
     * @param user The user entity who will own this category.
     * @return The created category as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If a category already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    CategoryReadOnlyDTO saveCategory(CategoryInsertDTO dto, User user)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Updates an existing category.
     * @param uuid The unique identifier of the category to update.
     * @param dto The updated data.
     * @param user The user entity who will own this category.
     * @return The updated category.
     * @throws EntityNotFoundException If a category not found.
     * @throws EntityAlreadyExistsException If a category already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    CategoryReadOnlyDTO updateCategory(UUID uuid, CategoryEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a category by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this category.
     * @throws EntityNotFoundException If a category not found.
     */
    void deleteCategory(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a category by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this category.
     * @return The category details.
     * @throws EntityNotFoundException If a category not found.
     */
    CategoryReadOnlyDTO getCategoryByUuid(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all categories.
     * @param pageable Contains page number, size, and sorting info.
     * @param user The user entity who will own this category.
     * @return A page of CategoryReadOnlyDTO.
     */
    Page<CategoryReadOnlyDTO> getAllCategories(Pageable pageable, User user);


    /**
     * Checks if a category exists for the given user.
     * @param category The name of the category.
     * @param user The user entity who will own this category.
     * @return True if it exists.
     */
    boolean isCategoryExists(String category, User user);
}