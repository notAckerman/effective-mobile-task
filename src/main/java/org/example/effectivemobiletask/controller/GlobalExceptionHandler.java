package org.example.effectivemobiletask.controller;

import org.example.effectivemobiletask.util.exception.AccessDeniedException;
import org.example.effectivemobiletask.util.exception.DuplicateException;
import org.example.effectivemobiletask.util.exception.InvalidOperationException;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.example.effectivemobiletask.model.dto.exception.ExceptionResponse;
import org.example.effectivemobiletask.util.CreatorsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
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
}
