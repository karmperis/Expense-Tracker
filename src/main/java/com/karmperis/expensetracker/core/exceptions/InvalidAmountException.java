package com.karmperis.expensetracker.core.exceptions;

public class InvalidAmountException extends AppGenericException {
    private static final String DEFAULT_CODE = "INVALID_AMOUNT";

    /**
     * InvalidAmountException with a default error code.
     * @param message The error description.
     */
    public InvalidAmountException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * InvalidAmountException with a custom error code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public InvalidAmountException(String code, String message) {
        super(code, message);
    }
}