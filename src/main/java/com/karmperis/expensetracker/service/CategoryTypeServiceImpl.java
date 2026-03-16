package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.CategoryTypeEditDTO;
import com.karmperis.expensetracker.dto.CategoryTypeInsertDTO;
import com.karmperis.expensetracker.dto.CategoryTypeReadOnlyDTO;
import com.karmperis.expensetracker.mapper.CategoryTypeMapper;
import com.karmperis.expensetracker.model.CategoryType;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the ICategoryTypeService for managing category types.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryTypeServiceImpl implements ICategoryTypeService {

    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryTypeMapper mapper;

    /**
     * Creates a new category type for a specific user.
     * @param dto  Data for the new category type.
     * @param user The owner of the record.
     * @return The saved category type as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If the type name already exists for this user.
     */
    @Override
    @Transactional(rollbackFor = EntityAlreadyExistsException.class)
    public CategoryTypeReadOnlyDTO saveCategoryType(CategoryTypeInsertDTO dto, User user)
            throws EntityAlreadyExistsException {

        try {
            if (categoryTypeRepository.existsByUserAndType(user, dto.categoryType())) {
                throw new EntityAlreadyExistsException("Category type", "Type " + dto.categoryType() + " already exists.");
            }

            CategoryType categoryType = mapper.mapToEntity(dto);
            categoryType.setUser(user);
            categoryType.setActive(true);

            categoryTypeRepository.save(categoryType);
            log.info("Category type '{}' saved successfully for user '{}'.", dto.categoryType(), user.getUsername());

            return mapper.mapToReadOnlyDTO(categoryType);

        } catch (EntityAlreadyExistsException e) {
            log.error("Failed to save Category type: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Updates an existing category type.
     * @param uuid Identification of the category type.
     * @param dto  The updated data.
     * @param user The owner of the record.
     * @return Updated ReadOnlyDTO.
     * @throws EntityNotFoundException If not found.
     * @throws EntityAlreadyExistsException If the new name is already taken.
     */
    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public CategoryTypeReadOnlyDTO updateCategoryType(UUID uuid, CategoryTypeEditDTO dto, User user)
            throws EntityNotFoundException, EntityAlreadyExistsException {

        try {
            CategoryType existingType = categoryTypeRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Category type", "UUID: " + uuid + " not found."));

            if (!existingType.getType().equals(dto.categoryType()) &&
                    categoryTypeRepository.existsByUserAndType(user, dto.categoryType())) {
                throw new EntityAlreadyExistsException("Category type", "Type " + dto.categoryType() + " already exists.");
            }


            mapper.updateEntityFromEditDTO(existingType, dto);

            categoryTypeRepository.save(existingType);
            log.info("Category type with uuid={} updated successfully.", uuid);

            return mapper.mapToReadOnlyDTO(existingType);

        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Update failed for Category type uuid={}: {}", uuid, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteCategoryType(UUID uuid, User user) throws EntityNotFoundException {
        try {
            CategoryType categoryType = categoryTypeRepository.findByUuidAndUser(uuid, user)
                    .orElseThrow(() -> new EntityNotFoundException("Category type", "UUID: " + uuid + " not found."));

            categoryType.setActive(false); // Soft delete
            log.info("Category type with uuid={} deactivated successfully.", uuid);
        } catch (EntityNotFoundException e) {
            log.error("Deactivation failed for uuid={}", uuid, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryTypeReadOnlyDTO getCategoryTypeByUuid(UUID uuid, User user) throws EntityNotFoundException {
        return categoryTypeRepository.findByUuidAndUser(uuid, user)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("Category type", "UUID: " + uuid + " not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryTypeReadOnlyDTO> getAllCategoryTypes(Pageable pageable, User user) {
        return categoryTypeRepository.findByUserAndActiveTrueOrderByTypeAsc(user, pageable)
                .map(mapper::mapToReadOnlyDTO);

    }

    /**
     * Checks if a category type with a specific name exists for a given user.
     * @param type The name of the category type to check.
     * @param user The user owner.
     * @return True if it exists, false otherwise.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCategoryTypeExists(String type, User user) {
            return categoryTypeRepository.existsByUserAndType(user, type);
    }

}