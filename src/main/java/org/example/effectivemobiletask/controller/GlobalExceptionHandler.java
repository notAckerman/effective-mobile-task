package org.example.effectivemobiletask.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletask.model.dto.exception.ExceptionResponse;
import org.example.effectivemobiletask.util.CreatorsUtil;
import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.example.effectivemobiletask.util.exception.DuplicateException;
import org.example.effectivemobiletask.util.exception.InvalidOperationException;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        ExceptionResponse response = CreatorsUtil.createExceptionResponse(e, HttpStatus.NOT_FOUND, request);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateException(DuplicateException e, WebRequest request) {
        ExceptionResponse response = CreatorsUtil.createExceptionResponse(e, HttpStatus.CONFLICT, request);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ExceptionResponse response = CreatorsUtil.createExceptionResponse(e, HttpStatus.FORBIDDEN, request);

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        Map<String, String> errors = CreatorsUtil.createValidationExceptionResponse(e);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOperationException(InvalidOperationException e, WebRequest request) {
        ExceptionResponse response = CreatorsUtil.createExceptionResponse(e, HttpStatus.BAD_REQUEST, request);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Request body is missing or unreadable.")
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e, WebRequest request) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("The requested endpoint was not found.")
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
