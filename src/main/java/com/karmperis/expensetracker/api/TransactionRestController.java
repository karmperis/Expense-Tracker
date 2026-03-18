package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.core.exceptions.InsufficientFundsException;
import com.karmperis.expensetracker.dto.TransactionInsertDTO;
import com.karmperis.expensetracker.dto.TransactionReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.service.ITransactionService;
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
 * Rest Controller for managing transactions.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionRestController {

    private final ITransactionService transactionService;
    private final IUserService userService;

    /**
     * Records a new transaction.
     * @param dto The transaction details.
     * @return The created transaction details.
     * @throws EntityNotFoundException If any associated entity is missing.
     * @throws InsufficientFundsException If an expense exceeds the account balance.
     */
    @PostMapping
    public ResponseEntity<TransactionReadOnlyDTO> createTransaction(@Valid @RequestBody TransactionInsertDTO dto)
            throws EntityNotFoundException, InsufficientFundsException {

        User user = userService.getRawUserByUsername("admin");
        TransactionReadOnlyDTO created = transactionService.saveTransaction(dto, user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves all transactions.
     * @param page Page number.
     * @param size Number of records per page.
     * @return A paginated list of transactions.
     */
    @GetMapping
    public ResponseEntity<Page<TransactionReadOnlyDTO>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.getRawUserByUsername("admin");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable, user));
    }

    /**
     * Retrieves a specific transaction by its UUID.
     * @param uuid The unique identifier of the transaction.
     * @return The transaction details.
     * @throws EntityNotFoundException If the transaction is not found.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionReadOnlyDTO> getTransactionByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {

        User user = userService.getRawUserByUsername("admin");
        return ResponseEntity.ok(transactionService.getTransactionByUuid(uuid, user));
    }

    /**
     * Deletes (soft delete) a transaction and reverts the account balance.
     * @param uuid The UUID of the transaction to delete.
     * @return No Content.
     * @throws EntityNotFoundException If the transaction is not found.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID uuid) throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        transactionService.deleteTransaction(uuid, user);
        return ResponseEntity.noContent().build();
    }
}