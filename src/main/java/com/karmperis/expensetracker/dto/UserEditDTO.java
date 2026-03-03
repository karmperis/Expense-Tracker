package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for edit a user from frontend.
 * @param email The email of the user.Not blank, must follow a valid email format.
 * @param active Determines whether the user is active or not.
 */
public record UserEditDTO(
        @NotBlank(message = "The email cannot be blank.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                message = "Please provide a valid email address."
        )
        String email,

        Boolean active
) {
}