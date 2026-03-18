package com.karmperis.expensetracker.api;

import com.karmperis.expensetracker.core.exceptions.EntityAlreadyExistsException;
import com.karmperis.expensetracker.core.exceptions.EntityNotFoundException;
import com.karmperis.expensetracker.dto.PaymentMethodInsertDTO;
import com.karmperis.expensetracker.dto.PaymentMethodReadOnlyDTO;
import com.karmperis.expensetracker.model.User;
import com.karmperis.expensetracker.service.IPaymentMethodService;
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
 * Rest Controller for managing payment methods.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodRestController {

    private final IPaymentMethodService paymentMethodService;
    private final IUserService userService;

    /**
     * Creates a new payment method.
     * @param dto The payment method data.
     * @return The created payment method.
     * @throws EntityAlreadyExistsException If the name is already in use by the user.
     * @throws EntityNotFoundException If the user is not found.
     */
    @PostMapping
    public ResponseEntity<PaymentMethodReadOnlyDTO> createPaymentMethod(@Valid @RequestBody PaymentMethodInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        return new ResponseEntity<>(paymentMethodService.savePaymentMethod(dto, user), HttpStatus.CREATED);
    }

    /**
     * Retrieves payment methods.
     * @param page The page number.
     * @param size The items per page.
     * @return A paginated list of payment methods.
     */
    @GetMapping
    public ResponseEntity<Page<PaymentMethodReadOnlyDTO>> getAllPaymentMethods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = userService.getRawUserByUsername("admin");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods(pageable, user));
    }

    /**
     * Retrieves a payment method by its UUID.
     * @param uuid The identifier.
     * @return The payment method details.
     * @throws EntityNotFoundException If not found.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<PaymentMethodReadOnlyDTO> getPaymentMethodByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        return ResponseEntity.ok(paymentMethodService.getPaymentMethodByUuid(uuid, user));
    }

    /**
     * Deletes a payment method.
     * @param uuid The identifier.
     * @return No Content.
     * @throws EntityNotFoundException If not found.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable UUID uuid) throws EntityNotFoundException {
        User user = userService.getRawUserByUsername("admin");
        paymentMethodService.deletePaymentMethod(uuid, user);
        return ResponseEntity.noContent().build();
    }
}