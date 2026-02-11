package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for edit an Account from frontend.
 * @param account The name of the account. Not blank, must contain at least two characters.
 * @param active  Determines whether the account is active or not.
 */
public record AccountEditDTO(
        @NotBlank(message = "The account cannot be blank.")
        @Size(min = 2, message = "The account must contain at least two characters.")
        String account,

        Boolean active
) {
}