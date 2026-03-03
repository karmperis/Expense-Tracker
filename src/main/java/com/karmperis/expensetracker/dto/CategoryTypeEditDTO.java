package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for edit a category type from frontend.
 * @param categoryType The name of the category type. Not blank, must contain at least two characters.
 * @param active Determines whether the category type is active or not.
 */
public record CategoryTypeEditDTO(
        @NotBlank(message = "The category type cannot be blank.")
        @Size(min = 2, message = "The category type must contain at least two characters.")
        String categoryType,

        Boolean active
) {
}