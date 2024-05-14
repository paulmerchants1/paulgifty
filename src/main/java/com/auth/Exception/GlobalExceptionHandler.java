package com.auth.Exception;
import com.auth.controller.ApiResponse;
import com.auth.dto.Response;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements RequestBodyAdvice {
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
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
// Here we are getting all the error which is invalid as per our java bean Validation
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .toList();
// returning 400 Bad TokenRequestDTO in HTTP STATUS and all the invalid parameter details in body
        return ResponseEntity.ok().body(getResponse(String.valueOf(validationErrors), "400"));
    }
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
    /*----Template For Response----*/
//    private Response getResponse (String message, String code) {
//        System.out.println(message);
//        Response response = new Response();
//        response.setStatus("FAILURE");
//        response.setMessage(message);
//        response.setStatusCode(code);
//        response.setResponseType("E");
//        return response;
//    }
    private Response getResponse(String errorMessage, String code) {
        String message = extractErrorMessage(errorMessage);
        Response response = new Response();
        response.setStatus("FAILURE");
        response.setMessage(message);
        response.setStatusCode(code);
        response.setResponseType("E");
        return response;
    }
    private String extractErrorMessage(String errorMessage) {
        // Remove the "mobileNo :" prefix and the opening bracket "["
        int startIndex = errorMessage.indexOf(":") + 1;
        int endIndex = errorMessage.lastIndexOf("]");
        if (startIndex >= 0 && endIndex >= 0 && endIndex > startIndex) {
            return errorMessage.substring(startIndex, endIndex).trim();
        } else {
            return errorMessage;
        }
    }
}



