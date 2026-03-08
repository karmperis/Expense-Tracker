package com.karmperis.expensetracker.core.exceptions;

public class EntityNotFoundException extends AppGenericException {
    private static final String DEFAULT_CODE = "NOT_FOUND";

    /**
     * EntityNotFoundException with a default error code.
     * @param message The error description.
     */
    public EntityNotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * EntityNotFoundException with a custom error code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public EntityNotFoundException(String code, String message) {
        super(code, message);
    }
}