package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for create a new user from frontend.
 * @param username The username of the user. Not blank, must be between 3 and 20 characters long.
 * @param email The email of the user. Not blank, must follow a valid email format.
 * @param password The password of the user. Not blank, must follow a valid password format.
 */
public record UserInsertDTO(
        @NotBlank(message = "The username cannot be blank.")
        @Size(min = 3, max = 20, message = "The username must be between 3 and 20 characters long.")
        String username,

        @NotBlank(message = "The email cannot be blank.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                message = "Please provide a valid email address."
        )
        String email,

        @NotBlank(message = "The password cannot be blank.")
        @Pattern(
                regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$",
                message = "Password must be at least 8 characters long and include uppercase, " +
                          "lowercase, a number, and a special character."
        )
        String password
) {
}