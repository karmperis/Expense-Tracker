package com.karmperis.expensetracker.dto;

import java.util.UUID;

/**
 *  DTO for representing a read-only view of a category.
 *  @param id The unique UUID of the category.
 *  @param category The name of the category.
 *  @param categoryTypeId The unique identifier (ID) of the associated category type.
 *  @param categoryTypeName The display name of the category type.
 *  @param active The status of the category.
 */
public record CategoryReadOnlyDTO(
        UUID id,
        String category,
        Long categoryTypeId,
        String categoryTypeName,
        Boolean active
) {
}