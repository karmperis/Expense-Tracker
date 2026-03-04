package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.CategoryEditDTO;
import com.karmperis.expensetracker.dto.CategoryInsertDTO;
import com.karmperis.expensetracker.dto.CategoryReadOnlyDTO;
import com.karmperis.expensetracker.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    /**
     * Maps CategoryInsertDTO to a category entity.
     * @param dto Containing the category details.
     * @return A new category entity populated with the DTO data.
     */
    public Category mapToEntity(CategoryInsertDTO dto) {
        Category category = new Category();
        category.setCategory(dto.category());
        category.setActive(true);
        return category;
    }

    /**
     * Maps a category entity to a CategoryReadOnlyDTO.
     * @param category The category entity to be mapped.
     * @return A CategoryReadOnlyDTO
     */
    public CategoryReadOnlyDTO mapToReadOnlyDTO(Category category) {
        return new CategoryReadOnlyDTO(
                category.getUuid(),
                category.getCategory(),
                category.getCategoryType().getId(),
                category.getCategoryType().getType(),
                category.getActive()
        );
    }

    /**
     * Updates an existing category entity using data from a CategoryEditDTO.
     * @param category The existing category entity to be updated.
     * @param dto CategoryEditDTO
     */
    public void updateEntityFromEditDTO(Category category, CategoryEditDTO dto) {
        category.setCategory(dto.category());
        if (dto.active() != null) {
            category.setActive(dto.active());
        }
    }
}