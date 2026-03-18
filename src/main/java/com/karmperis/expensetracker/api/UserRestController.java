package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityInvalidArgumentException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.UserInsertDTO;
import com.karmperis.expensetracker.dto.UserReadOnlyDTO;
import com.karmperis.expensetracker.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest controller for managing users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final IUserService userService;

    /**
     * Registers a new user.
     * @param dto The user data for registration.
     * @return A ResponseEntity containing the created UserReadOnlyDTO.
     * @throws EntityAlreadyExistsException If the username or email is already taken.
     * @throws EntityInvalidArgumentException If the registration data is invalid.
     */
    @PostMapping("/register")
    public ResponseEntity<UserReadOnlyDTO> registerUser(@Valid @RequestBody UserInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        UserReadOnlyDTO createdUser = userService.saveUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of users.
     * @param page The page number (default 0).
     * @param size The items per page (default 10).
     * @return A ResponseEntity with a page of users.
     */
    @GetMapping
    public ResponseEntity<Page<UserReadOnlyDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    /**
     * Retrieves a specific user by their UUID.
     * @param uuid The UUID of the user.
     * @return The user details.
     * @throws EntityNotFoundException If the user is not found.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> getUserByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        return ResponseEntity.ok(userService.getUserByUuid(uuid));
    }

    /**
     * Retrieves a user by username.
     * @param username The username to search for.
     * @return The user details.
     * @throws EntityNotFoundException If the user is not found.
     */
    @GetMapping("/search")
    public ResponseEntity<UserReadOnlyDTO> getUserByUsername(@RequestParam String username)
            throws EntityNotFoundException {

        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}