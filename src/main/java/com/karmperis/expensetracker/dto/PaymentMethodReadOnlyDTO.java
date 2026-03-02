package com.karmperis.expensetracker.dto;

import java.util.UUID;

/**
 * DTO for representing a read-only view of a payment method.
 * @param id The unique UUID of the payment method.
 * @param paymentMethod The name of the payment method.
 * @param active The status of the payment method.
 */
public record PaymentMethodReadOnlyDTO(
        UUID id,
        String paymentMethod,
        Boolean active
) {
}