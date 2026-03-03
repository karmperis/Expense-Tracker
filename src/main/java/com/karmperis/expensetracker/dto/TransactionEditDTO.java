package com.karmperis.expensetracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for edit a transaction from frontend
 * @param date The date of the transaction. Not null, is required.
 * @param categoryId The id of the category. Not null, is required.
 * @param amount The amount of the transaction. Must be positive.
 * @param description The description of the transaction.
 * @param paymentMethodId The id of the payment method. Not null, is required.
 * @param accountId The id of the account. Not null, is required.
 * @param active Determines whether the transaction is active or not.
 */
public record TransactionEditDTO(

        @NotNull(message = "Date is required.")
        LocalDate date,

        @NotNull(message = "Category id is required.")
        Long categoryId,

        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be positive.")
        BigDecimal amount,

        @Size(max = 150)
        String description,

        @NotNull(message = "Payment method id is required.")
        Long paymentMethodId,

        @NotNull(message = "Account id is required.")
        Long accountId,

        Boolean active
) {
}