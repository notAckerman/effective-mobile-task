package org.example.effectivemobiletask.util;

import org.example.effectivemobiletask.model.dto.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CreatorsUtil {

    public static ExceptionResponse createExceptionResponse(Exception e, HttpStatus status, WebRequest request) {
        return ExceptionResponse.builder()
                .status(status.value())
                .error(e.getClass().getSimpleName())
                .path(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
    }

    public static Map<String, String> createValidationExceptionResponse(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.put(field, message);
        });
        return errors;
    }
}
