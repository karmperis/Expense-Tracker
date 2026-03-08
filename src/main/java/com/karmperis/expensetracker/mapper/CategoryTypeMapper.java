package com.karmperis.expensetracker.mapper;

import com.karmperis.expensetracker.dto.CategoryTypeEditDTO;
import com.karmperis.expensetracker.dto.CategoryTypeInsertDTO;
import com.karmperis.expensetracker.dto.CategoryTypeReadOnlyDTO;
import com.karmperis.expensetracker.model.CategoryType;
import org.springframework.stereotype.Component;

@Component
public class CategoryTypeMapper {

    /**
     * Maps CategoryTypeInsertDTO to a category type entity.
     * @param dto Containing the category type details.
     * @return A new category type entity populated with the DTO data.
     */
    public CategoryType mapToEntity(CategoryTypeInsertDTO dto) {
        CategoryType categoryType = new CategoryType();
        categoryType.setType(dto.categoryType());
        categoryType.setActive(true);
        return categoryType;
    }

    /**
     * Maps a category type entity to a CategoryTypeReadOnlyDTO.
     * @param categoryType The category type entity to be mapped.
     * @return A CategoryTypeReadOnlyDTO.
     */
    public CategoryTypeReadOnlyDTO mapToReadOnlyDTO(CategoryType categoryType) {
        return new CategoryTypeReadOnlyDTO(
                categoryType.getUuid(),
                categoryType.getType(),
                categoryType.getActive()
        );
    }

    /**
     * Updates an existing category type entity using data from a CategoryTypeEditDTO.
     * @param categoryType The existing categoryType entity to be updated.
     * @param dto CategoryTypeEditDTO.
     */
    public void updateEntityFromEditDTO(CategoryType categoryType, CategoryTypeEditDTO dto) {
        categoryType.setType(dto.categoryType());
        if (dto.active() != null) {
            categoryType.setActive(dto.active());
        }
    }
}