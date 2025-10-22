package com.github.bobabnoba.loancalculator.web.error;

import com.github.bobabnoba.loancalculator.domain.error.DomainException;
import com.github.bobabnoba.loancalculator.domain.error.InvalidLoanSpecException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(", "));

        var body = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", message, req.getRequestURI(), "VALIDATION_ERROR");
        log.warn("400 Validation error at {}: {}", req.getRequestURI(), message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        String message = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.joining(", "));

        var body = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", message, req.getRequestURI(), "VALIDATION_ERROR");
        log.warn("400 Constraint violation at {}: {}", req.getRequestURI(), message);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleBadInput(Exception ex, HttpServletRequest req) {
        var body = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Malformed or incompatible request.", req.getRequestURI(), "BAD_REQUEST");
        log.warn("400 Bad input at {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMedia(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        var body = ErrorResponse.of(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Unsupported Media Type", ex.getMessage(), req.getRequestURI(), "UNSUPPORTED_MEDIA_TYPE");
        log.warn("415 at {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        var body = ErrorResponse.of(HttpStatus.CONFLICT.value(), "Conflict", "Data integrity violation.", req.getRequestURI(), "DATA_INTEGRITY_VIOLATION");
        log.warn("409 Data integrity at {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler({InvalidLoanSpecException.class, DomainException.class})
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex, HttpServletRequest req) {
        var body = ErrorResponse.of(422, "Unprocessable Entity", ex.getMessage(), req.getRequestURI(), "DOMAIN_ERROR");
        log.warn("422 Domain error at {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(422).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        var body = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "An unexpected error occurred.", req.getRequestURI(), "UNEXPECTED_ERROR");
        log.error("500 Unexpected error at {}", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(new HttpHeaders()).body(body);
    }
}