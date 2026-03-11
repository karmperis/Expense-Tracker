package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.*;
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
     * @return The created category as a ReadOnlyDTO.
     */
    CategoryReadOnlyDTO saveCategory(CategoryInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Updates an existing category.
     * @param uuid The unique identifier of the category to update.
     * @param dto The updated data.
     * @return The updated category.
     */
    CategoryReadOnlyDTO updateCategory(UUID uuid, CategoryEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a category by its UUID.
     * @param uuid The unique identifier.
     */
    void deleteCategory(UUID uuid) throws EntityNotFoundException;

    /**
     * Retrieves a category by its UUID.
     * @param uuid The unique identifier.
     * @return The category details.
     */
    CategoryReadOnlyDTO getCategoryByUuid(UUID uuid) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all categories.
     * @param pageable Contains page number, size, and sorting info.
     * @return A page of CategoryReadOnlyDTO.
     */
    Page<CategoryReadOnlyDTO> getAllCategories(Pageable pageable);

    boolean isCategoryExists(String category);
}