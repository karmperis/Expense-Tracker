package com.karmperis.expensetracker.dto;

import java.util.UUID;

/**
 * DTO for representing a read-only view of a user.
 * @param uuid The unique UUID of the user.
 * @param username The username of the user.
 * @param email The email of the user.
 * @param active The status of the user.
 */
public record UserReadOnlyDTO(
        UUID uuid,
        String username,
        String email,
        Boolean active
) {
}