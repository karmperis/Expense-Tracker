package com.karmperis.expensetracker.dto;

import java.util.UUID;

/**
 * DTO for representing a read-only view of a category type.
 * @param id The unique UUID of the category type.
 * @param type The name of the category type.
 * @param active The status of the category type.
 */
public record CategoryTypeReadOnlyDTO(
        UUID id,
        String type,
        Boolean active
) {
}