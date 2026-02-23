package com.karmperis.expensetracker.repository;

import com.karmperis.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing user entities.
 */
public interface UserRepository extends JpaRepository <User, Long> {
    /**
     * Retrieves a user based on their UUID.
     * @param uuid The UUID of the user to search for.
     * @return The found user, or empty if no matching user is found.
     */
    Optional<User> findByUuid(UUID uuid);

    /**
     * Retrieves a user by their username.
     * @param username The username to search for.
     * @return The found user, or empty if no matching username is found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by their email.
     * @param email The email to search for.
     * @return The found user, or empty if no matching email is found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username already exists.
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email already exists.
     * @param email The email to check.
     * @return True if the email is already exists, false otherwise.
     */
    boolean existsByEmail(String email);
}