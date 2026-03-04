package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for create a new payment method from frontend.
 * @param paymentMethod The name of the payment method. Not blank, must contain at least two characters.
 */
public record PaymentMethodInsertDTO(
        @NotBlank(message = "The payment method cannot be blank.")
        @Size(min = 2, message = "The payment method must contain at least two characters.")
        String paymentMethod
) {
}