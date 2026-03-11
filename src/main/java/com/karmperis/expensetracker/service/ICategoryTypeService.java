package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.CategoryTypeEditDTO;
import com.karmperis.expensetracker.dto.CategoryTypeInsertDTO;
import com.karmperis.expensetracker.dto.CategoryTypeReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/**
 * Interface for category type service.
 */
public interface ICategoryTypeService {
    /**
     * Creates a new category type.
     * @param dto Data for the new category type.
     * @param user The user entity who will own this category type.
     * @return The created type category as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If a category type already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    CategoryTypeReadOnlyDTO saveCategoryType(CategoryTypeInsertDTO dto, User user)
        throws EntityAlreadyExistsException, EntityInvalidArgumentException;


    /**
     * Updates an existing category type.
     * @param uuid The unique identifier of the category type to update.
     * @param dto The updated data.
     * @param user The user entity who will own this category type.
     * @return The updated category type.
     * @throws EntityNotFoundException If a category type not found.
     * @throws EntityAlreadyExistsException If a category type already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    CategoryTypeReadOnlyDTO updateCategoryType(UUID uuid, CategoryTypeEditDTO dto, User user)
        throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a category type by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this category type.
     * @throws EntityNotFoundException If a category type not found.
     */
    void deleteCategoryType(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a category type by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this category type.
     * @return The category type details.
     * @throws EntityNotFoundException If a category type not found.
     */
    CategoryTypeReadOnlyDTO getCategoryTypeByUuid(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all category types.
     * @param pageable Contains page number, size, and sorting info.
     * @param user The user entity who will own this category type.
     * @return A page of CategoryTypeReadOnlyDTO.
     */
    Page<CategoryTypeReadOnlyDTO> getAllCategoryTypes(Pageable pageable, User user);

    /**
     * Checks if a category type exists for the given user.
     * @param type The name of the category type.
     * @param user The user entity who will own this category type.
     * @return True if it exists.
     */
    boolean isCategoryTypeExists(String type, User user);
}