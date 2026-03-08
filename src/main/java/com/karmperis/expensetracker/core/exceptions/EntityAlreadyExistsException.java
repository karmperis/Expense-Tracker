package com.karmperis.expensetracker.core.exceptions;

public class EntityAlreadyExistsException extends AppGenericException {
    private static final String DEFAULT_CODE = "ALREADY_EXISTS";

    /**
     * EntityAlreadyExistsException with a default error code.
     * @param message The error description.
     */
    public EntityAlreadyExistsException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * EntityAlreadyExistsException with a custom error code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public EntityAlreadyExistsException(String code, String message) {
        super(code, message);
    }
}