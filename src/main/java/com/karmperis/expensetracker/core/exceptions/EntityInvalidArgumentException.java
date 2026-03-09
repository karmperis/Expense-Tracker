package com.karmperis.expensetracker.core.exceptions;

public class EntityInvalidArgumentException extends AppGenericException {
    private static final String DEFAULT_CODE = "INVALID_ARGUMENT";

    /**
     * EntityInvalidArgumentException with a default error code.
     * @param message The error description.
     */
    public EntityInvalidArgumentException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * EntityInvalidArgumentException with a custom error code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public EntityInvalidArgumentException(String code, String message) {
        super(code, message);
    }
}