package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.AccountEditDTO;
import com.karmperis.expensetracker.dto.AccountInsertDTO;
import com.karmperis.expensetracker.dto.AccountReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.service.IAccountService;
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
 * REST Controller for managing accounts.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountRestController {

    private final IAccountService accountService;
    private final IUserService userService;

    /**
     * Creates a new account.
     * @param dto The account data including name and initial balance.
     * @return The created account details.
     * @throws EntityAlreadyExistsException If an account with the same name already exists.
     * @throws EntityNotFoundException If the associated user is not found.
     */
    @PostMapping
    public ResponseEntity<AccountReadOnlyDTO> createAccount(@Valid @RequestBody AccountInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        return new ResponseEntity<>(accountService.saveAccount(dto, user), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of all active accounts for the user.
     * @param page The page number (default 0).
     * @param size The items per page (default 10).
     * @return A ResponseEntity with a page of account data.
     */
    @GetMapping
    public ResponseEntity<Page<AccountReadOnlyDTO>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = userService.getRawUserByUsername("admin");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(accountService.getAllAccounts(pageable, user));
    }

    /**
     * Retrieves a specific account by its unique UUID.
     * @param uuid The unique identifier of the account.
     * @return The account details.
     * @throws EntityNotFoundException If the account does not exist or does not belong to the user.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<AccountReadOnlyDTO> getAccountByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        return ResponseEntity.ok(accountService.getAccountByUuid(uuid, user));
    }

    /**
     * Updates an existing account.
     * @param uuid The UUID of the account to update.
     * @param dto  The updated data.
     * @return The updated account details.
     * @throws EntityNotFoundException If the account is not found.
     * @throws EntityAlreadyExistsException If the new name conflicts with an existing account.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<AccountReadOnlyDTO> updateAccount(
            @PathVariable UUID uuid, @Valid @RequestBody AccountEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        User user = userService.getRawUserByUsername("admin");
        return ResponseEntity.ok(accountService.updateAccount(uuid, dto, user));
    }

    /**
     * Deactivates (soft delete) an account.
     * @param uuid The UUID of the account to delete.
     * @return No content response on success.
     * @throws EntityNotFoundException If the account is not found.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID uuid) throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        accountService.deleteAccount(uuid, user);
        return ResponseEntity.noContent().build();
    }
}