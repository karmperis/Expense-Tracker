package com.karmperis.expensetracker.service;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.UserEditDTO;
import com.karmperis.expensetracker.dto.UserInsertDTO;
import com.karmperis.expensetracker.dto.UserReadOnlyDTO;
import com.karmperis.expensetracker.mapper.UserMapper;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the IUserService for managing user entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Saves a new user after hashing the password and checking for uniqueness.
     * @param dto Data for the new user.
     * @return The created user as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If username or email is already taken.
     */
    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            if (userRepository.existsByUsername(dto.username())) {
                throw new EntityAlreadyExistsException("User", "Username '" + dto.username() + "' is already taken.");
            }
            if (userRepository.existsByEmail(dto.email())) {
                throw new EntityAlreadyExistsException("User", "Email '" + dto.email() + "' is already registered.");
            }

            User user = mapper.mapToEntity(dto);

            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setActive(true);

            userRepository.save(user);
            log.info("User with username: {} registered successfully.", dto.username());

            return mapper.mapToReadOnlyDTO(user);
        } catch (EntityAlreadyExistsException e) {
            log.error("Registration failed for user: {}", dto.username(), e);
            throw e;
        }
    }

    /**
     * Updates an existing user's details.
     * @param uuid The unique identifier of the user to update.
     * @param dto The updated data.
     * @return The updated user details.
     * @throws EntityNotFoundException If user does not exist.
     * @throws EntityAlreadyExistsException If the new email is already taken.
     */
    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public UserReadOnlyDTO updateUser(UUID uuid, UserEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            User existingUser = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User not found with uuid: " + uuid));

            if (!existingUser.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
                throw new EntityAlreadyExistsException("User", "Email '" + dto.email() + "' is already in use.");
            }

            mapper.updateEntityFromEditDTO(existingUser, dto);
            userRepository.save(existingUser);

            log.info("User with uuid: {} updated successfully.", uuid);
            return mapper.mapToReadOnlyDTO(existingUser);
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            log.error("Update failed for user uuid: {}", uuid, e);
            throw e;
        }
    }

    /**
     * Soft deletes a user by setting active status to false.
     * @param uuid The unique identifier of the user.
     * @throws EntityNotFoundException If user is not found.
     */
    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteUser(UUID uuid) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User", "User not found with uuid: " + uuid));

            user.setActive(false);
            log.info("User with uuid: {} deactivated successfully.", uuid);
        } catch (EntityNotFoundException e) {
            log.error("Deactivation failed for uuid: {}", uuid, e);
            throw e;
        }
    }

    /**
     * Retrieves a user by their UUID.
     * @param uuid The unique identifier.
     * @return User details.
     * @throws EntityNotFoundException If user is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUuid(UUID uuid) throws EntityNotFoundException {
        return userRepository.findByUuid(uuid)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("User", "User not found with uuid: " + uuid));
    }

    /**
     * Retrieves a user by their username.
     * @param username The username to search for.
     * @return User details.
     * @throws EntityNotFoundException If user is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .map(mapper::mapToReadOnlyDTO)
                .orElseThrow(() -> new EntityNotFoundException("User", "User with username '" + username + "' not found."));
    }

    /**
     * Retrieves a paginated list of all users.
     * @param pageable Pagination info.
     * @return A page of users.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserReadOnlyDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(mapper::mapToReadOnlyDTO);
    }

    /**
     * Checks if a username exists.
     * @param username The username to check.
     * @return True if exists, false otherwise.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}