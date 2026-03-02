package com.karmperis.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for representing a read-only view of a transaction.
 * @param uuid The unique UUID of the transaction.
 * @param date The date of the transaction.
 * @param categoryId The unique identifier (ID) of the category.
 * @param categoryName The category name of the transaction.
 * @param amount The amount of the transaction.
 * @param description The description of the transaction.
 * @param paymentMethodId The unique identifier (ID) of the payment method.
 * @param paymentMethodName The payment method name of the transaction.
 * @param accountId The unique identifier (ID) of the account.
 * @param accountName The account name of the transaction.
 * @param active The status of the transaction.
 */
public record TransactionReadOnlyDTO(
        UUID uuid,
        LocalDate date,
        Long categoryId,
        String categoryName,
        BigDecimal amount,
        String description,
        Long paymentMethodId,
        String paymentMethodName,
        Long accountId,
        String accountName,
        Boolean active
) {
}