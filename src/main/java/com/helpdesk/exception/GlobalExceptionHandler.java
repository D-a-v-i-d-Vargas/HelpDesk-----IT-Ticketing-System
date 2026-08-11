package com.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> resourceNotFoundException(ResourceNotFoundException e){

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getMessage()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> noValidException(MethodArgumentNotValidException e){

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldError = ((FieldError)objectError).getField();
            String defaultError = objectError.getDefaultMessage();

            errors.put(fieldError,defaultError);
        });

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getMessage(),
                errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
