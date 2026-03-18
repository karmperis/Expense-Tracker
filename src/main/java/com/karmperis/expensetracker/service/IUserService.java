package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.*;
import com.karmperis.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface for user service.
 */
public interface IUserService {
    /**
     * Saves a new user.
     * @param dto Data for the new user.
     * @return The created user as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If a user already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    UserReadOnlyDTO saveUser(UserInsertDTO dto)
        throws EntityAlreadyExistsException, EntityInvalidArgumentException;


    /**
     * Updates an existing user.
     * @param uuid The unique identifier of the user to update.
     * @param dto The updated data.
     * @return The updated user.
     * @throws EntityNotFoundException If a user not found.
     * @throws EntityAlreadyExistsException If a user already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    UserReadOnlyDTO updateUser(UUID uuid, UserEditDTO dto)
        throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a user by its UUID.
     * @param uuid The unique identifier.
     * @throws EntityNotFoundException If a user not found.
     */
    void deleteUser(UUID uuid) throws EntityNotFoundException;

    /**
     * Retrieves a user by its UUID.
     * @param uuid The unique identifier.
     * @return The user details.
     * @throws EntityNotFoundException If a user not found.
     */
    UserReadOnlyDTO getUserByUuid(UUID uuid) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all users.
     * @param pageable Contains page number, size, and sorting info.
     * @return A page of UserReadOnlyDTO.
     */
    Page<UserReadOnlyDTO> getAllUsers(Pageable pageable);

    /**
     * Retrieves a user by their username.
     * @param username The username to search for.
     * @return The user details.
     * @throws EntityNotFoundException If the user is not found.
     */
    UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException;

    /**
     * Checks if a username is already taken.
     * @param username The name of the user.
     * @return True if it exists.
     */
    boolean isUserNameExists(String username);

    /**
     * Retrieves the user entity by username.
     * This is used internally to associate other entities with a specific user.
     * @param username The username to search for.
     * @return The user entity.
     * @throws EntityNotFoundException If the user does not exist.
     */
    User getRawUserByUsername(String username) throws EntityNotFoundException;
}