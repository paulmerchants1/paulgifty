package com.auth.Exception;

public class InvalidMobileNumberException extends RuntimeException {

    public InvalidMobileNumberException(String message) {
        super(message);
    }
}