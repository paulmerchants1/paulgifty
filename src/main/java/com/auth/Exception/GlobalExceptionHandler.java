package com.auth.Exception;


import com.auth.controller.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleDuplicateUserException(DuplicateUserException ex) {
        return new ApiResponse(ex.getMessage(), null);
    }

    // Add more exception handlers for other custom exceptions if needed

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse handDataIntegrityException(DataIntegrityViolationException ex) {
        return new ApiResponse(ex.getMessage(), null);
    }


    @ExceptionHandler(GiftyAPIException.class)
    public ApiResponse handleGiftyAPIException(GiftyAPIException ex) {
        return new ApiResponse(ex.getMessage(), null);
    }

}
