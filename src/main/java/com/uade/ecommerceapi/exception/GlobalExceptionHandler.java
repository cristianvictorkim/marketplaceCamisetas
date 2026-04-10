package com.uade.ecommerceapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("error", "NOT_FOUND");
        body.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> validations = new LinkedHashMap<String, String>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            validations.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");
        body.put("details", validations);
        return ResponseEntity.badRequest().body(body);
    }
}
