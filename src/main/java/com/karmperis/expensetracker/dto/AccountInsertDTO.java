package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for create a new Account from frontend.
 * @param account The name of the account. Not blank, must contain at least two characters.
 */
public record AccountInsertDTO(
        @NotBlank(message = "The account cannot be blank.")
        @Size(min = 2, message = "The account must contain at least two characters.")
        String account
) {
}