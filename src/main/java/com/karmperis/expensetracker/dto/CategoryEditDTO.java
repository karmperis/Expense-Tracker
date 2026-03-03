package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *  DTO for edit a category from frontend.
 * @param category The name of the category. Not blank, must contain at least two characters.
 * @param categoryTypeId The id of the category type. Not null, is required.
 * @param active Determines whether the category is active or not.
 */
public record CategoryEditDTO(
        @NotBlank(message = "The category cannot be blank.")
        @Size(min = 2, message = "The category must contain at least two characters.")
        String category,

        @NotNull(message = "The category type is required.")
                Long categoryTypeId,

        Boolean active
) {
}