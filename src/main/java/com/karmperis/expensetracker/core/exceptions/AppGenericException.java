package com.karmperis.expensetracker.core.exceptions;

import lombok.Getter;

/**
 * Generic class for all custom runtime exceptions in the application.
 */
@Getter
public class AppGenericException extends RuntimeException{
    private final String code;

    /**
     * AppGenericException with a specific code and message.
     * @param code The error code.
     * @param message The error description.
     */
    public AppGenericException(String code, String message){
        super(message);
        this.code = code;
    }
}