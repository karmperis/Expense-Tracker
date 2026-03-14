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
 * Interface for payment method service.
 */
public interface IPaymentMethodService {
    /**
     * Creates a new payment method.
     * @param dto Data for the new payment method.
     * @param user The user entity who will own this payment method.
     * @return The created payment method as a ReadOnlyDTO.
     * @throws EntityAlreadyExistsException If a payment method already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    PaymentMethodReadOnlyDTO savePaymentMethod(PaymentMethodInsertDTO dto, User user)
        throws EntityAlreadyExistsException, EntityInvalidArgumentException;


    /**
     * Updates an existing payment method.
     * @param uuid The unique identifier of the payment method to update.
     * @param dto The updated data.
     * @param user The user entity who will own this payment method.
     * @return The updated payment method.
     * @throws EntityNotFoundException If a payment method not found.
     * @throws EntityAlreadyExistsException If a payment method already exists.
     * @throws EntityInvalidArgumentException If the provided data is invalid or fails business rule validation.
     */
    PaymentMethodReadOnlyDTO updatePaymentMethod(UUID uuid, PaymentMethodEditDTO dto, User user)
        throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Deletes a payment method by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this payment method.
     * @throws EntityNotFoundException If a payment method not found.
     */
    void deletePaymentMethod(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a payment method by its UUID.
     * @param uuid The unique identifier.
     * @param user The user entity who will own this payment method.
     * @return The payment method details.
     * @throws EntityNotFoundException If a payment method not found.
     */
    PaymentMethodReadOnlyDTO getPaymentMethodByUuid(UUID uuid, User user) throws EntityNotFoundException;

    /**
     * Retrieves a paginated list of all payment methods.
     * @param pageable Contains page number, size, and sorting info.
     * @param user The user entity who will own this payment method.
     * @return A page of PaymentMethodReadOnlyDTO.
     */
    Page<PaymentMethodReadOnlyDTO> getAllPaymentMethods(Pageable pageable, User user);

    /**
     * Checks if a payment method exists for the given user.
     * @param paymentMethod The name of the payment method.
     * @param user The user entity who will own this payment method.
     * @return True if it exists.
     */
    boolean isPaymentMethodExists(String paymentMethod, User user);
}