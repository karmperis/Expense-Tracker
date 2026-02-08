package com.karmperis.expensetracker.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *  DTO for representing a read-only view of an Account.
 *  @param id The unique UUID of the account.
 *  @param account The name of the account.
 *  @param balance The current balance of the account.
 *  @param active  The status of the account.
 */
public record AccountReadOnlyDTO(
        UUID id,
        String account,
        BigDecimal balance,
        Boolean active
) {
}