package com.karmperis.expensetracker.core.exceptions;

public class InsufficientFundsException extends AppGenericException {
    private static final String DEFAULT_CODE = "INSUFFICIENT_FUNDS";

    /**
     * InsufficientFundsException with a default error code.
     * @param message The error description.
     */
    public InsufficientFundsException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * InsufficientFundsException with a custom error code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public InsufficientFundsException(String code, String message) {
        super(code, message);
    }
}