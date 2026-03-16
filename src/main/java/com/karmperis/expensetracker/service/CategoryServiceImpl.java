package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.CategoryEditDTO;
import com.karmperis.expensetracker.dto.CategoryInsertDTO;
import com.karmperis.expensetracker.dto.CategoryReadOnlyDTO;
import com.karmperis.expensetracker.mapper.CategoryMapper;
import com.karmperis.expensetracker.model.Category;
import com.karmperis.expensetracker.model.CategoryType;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.repository.CategoryRepository;
import com.karmperis.expensetracker.repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the ICategoryService for managing categories.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final CategoryTypeRepository categoryTypeRepository;

    /**
     * Creates and saves a new category for a specific user.
     * @param dto  The data for the new category.
     * @param user The user who will own the category.
     * @return The created category as a CategoryReadOnlyDTO.
     * @throws EntityAlreadyExistsException If the category name is already taken by this user.
     * @throws EntityInvalidArgumentException If provided data fails validation.
     */
    @Override
    @Transactional(rollbackFor = { EntityAlreadyExistsException.class, EntityInvalidArgumentException.class })
    public CategoryReadOnlyDTO saveCategory(CategoryInsertDTO dto, User user)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            if (categoryRepository.existsByUserAndCategory(user, dto.category())) {
                throw new EntityAlreadyExistsException("Category", "Category with name " + dto.category() + " already exists");
            }

            Category category = mapper.mapToEntity(dto);

            CategoryType categoryType = categoryTypeRepository.findById(dto.categoryTypeId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("CategoryType", "Invalid ID"));
            category.setCategoryType(categoryType);

            category.setUser(user);
            category.setActive(true);

            categoryRepository.save(category);

            log.info("Category with name={} saved successfully for user={}.", dto.category(), user.getUsername());
            return mapper.mapToReadOnlyDTO(category);

        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed for category={}. Category already exists for user={}", dto.category(), user.getUsername(), e);
            throw e;
        }
    }

    /**
     * Updates an existing category.
     * @param uuid The unique identifier of the category to update.
     * @param dto The updated data.
     * @param user The user owner.
     * @return The updated category as a CategoryReadOnlyDTO.
     * @throws EntityNotFoundException If the category does not exist.
     * @throws EntityAlreadyExistsException If the new name is already used by another category.
     * @throws EntityInvalidArgumentException If the provided category type ID is invalid.
     */
    @Override
    @Transactional(rollbackFor = { EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class })
    public CategoryReadOnlyDTO updateCategory(UUID uuid, CategoryEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            Category existingCategory = categoryRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Category", "Category with uuid=" + uuid + " not found"));

            if (!existingCategory.getCategory().equals(dto.category()) &&
                    categoryRepository.existsByUserAndCategory(user, dto.category())) {
                throw new EntityAlreadyExistsException("Category", "Category with name " + dto.category() + " already exists");
            }

            existingCategory.setCategory(dto.category());
            if (!existingCategory.getCategoryType().getId().equals(dto.categoryTypeId())) {
                CategoryType newType = categoryTypeRepository.findById(dto.categoryTypeId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("CategoryType", "CategoryType id=" + dto.categoryTypeId() + " invalid"));
                existingCategory.setCategoryType(newType);
            }

            if (dto.active() != null) {
                existingCategory.setActive(dto.active());
            }

            categoryRepository.save(existingCategory);
            log.info("Category with uuid={} updated successfully", uuid);
            return mapper.mapToReadOnlyDTO(existingCategory);

        } catch (EntityNotFoundException | EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            log.error("Update failed for category with uuid={}", uuid, e);
            throw e;
        }
    }

    /**
     * Performs a soft delete by setting the category active status to false.
     * @param uuid The unique identifier of the category.
     * @param user The user owner.
     * @throws EntityNotFoundException If the category is not found.
     */
    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteCategory(UUID uuid, User user) throws EntityNotFoundException {
        try {
            Category category = categoryRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Category", "Category with uuid=" + uuid + " not found"));

            category.setActive(false);
            log.info("Category with uuid={} deleted successfully", uuid);
        } catch (EntityNotFoundException e) {
            log.error("Delete failed for category with uuid={}", uuid, e);
            throw e;
        }
    }

    /**
     * Retrieves a single category by its UUID.
     * @param uuid The unique identifier.
     * @param user The user owner.
     * @return {@link CategoryReadOnlyDTO} containing category details.
     * @throws EntityNotFoundException if the category is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryReadOnlyDTO getCategoryByUuid(UUID uuid, User user) throws EntityNotFoundException {
        try {
            Category category = categoryRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Category", "Category with uuid=" + uuid + " not found"));

            log.debug("Get category by uuid={} returned successfully", uuid);
            return mapper.mapToReadOnlyDTO(category);
        } catch (EntityNotFoundException e) {
            log.error("Get category by uuid={} failed", uuid, e);
            throw e;
        }
    }

    /**
     * Retrieves a paginated list of all active categories for a specific user.
     * @param pageable Pagination and sorting information.
     * @param user The user owner.
     * @return A page of CategoryReadOnlyDTO.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryReadOnlyDTO> getAllCategories(Pageable pageable, User user) {
        Page<Category> categoriesPage = categoryRepository.findByUserAndActiveTrueOrderByCategoryAsc(user, pageable);
        log.debug("Get paginated categories returned successfully for user={}", user.getUsername());
        return categoriesPage.map(mapper::mapToReadOnlyDTO);
    }

    /**
     * Checks if a category with a specific name exists for a given user.
     * @param category The name of the category to check.
     * @param user The user owner.
     * @return True if it exists, false otherwise.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCategoryExists(String category, User user) {
        return categoryRepository.existsByUserAndCategory(user, category);
    }
}