package com.auth.Exception;

public class DuplicateEntryException extends RuntimeException {
    private int statusCode;
    private String message;

    public DuplicateEntryException(int statusCode, String message, String duplicateEntry) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
