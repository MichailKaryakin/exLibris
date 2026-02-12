package org.example.exlibris.common.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.exlibris.book.exception.AccessDeniedBookOperationException;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.common.dto.ErrorResponse;
import org.example.exlibris.reading.exception.ReadingNotFoundException;
import org.example.exlibris.reading.exception.ReadingStateException;
import org.example.exlibris.security.exception.JwtAuthenticationException;
import org.example.exlibris.user.exception.EmailAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse createBody(HttpStatus status, String error, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message
        );
    }

    // ========== AUTH / USER ==========

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyUsedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createBody(HttpStatus.CONFLICT, "EMAIL_ALREADY_USED", e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createBody(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS", "Invalid email or password"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createBody(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", e.getMessage()));
    }

    // ========== SECURITY ==========

    @ExceptionHandler({JwtException.class, JwtAuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleJwtError(io.jsonwebtoken.JwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createBody(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", e.getMessage()));
    }

    // ========== BOOK ==========

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createBody(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedBookOperationException.class)
    public ResponseEntity<ErrorResponse> handleBookAccessDenied(AccessDeniedBookOperationException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(createBody(HttpStatus.FORBIDDEN, "BOOK_ACCESS_DENIED", e.getMessage()));
    }

    // ========== READING ==========

    @ExceptionHandler(ReadingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadingNotFound(ReadingNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createBody(HttpStatus.NOT_FOUND, "READING_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(ReadingStateException.class)
    public ResponseEntity<ErrorResponse> handleReadingState(ReadingStateException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createBody(HttpStatus.CONFLICT, "READING_STATE_ERROR", e.getMessage()));
    }

    // ========== VALIDATION ==========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(createBody(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", errors));
    }

    // ========== FALLBACK ==========

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> fallback(Exception e) {
        log.error("Unhandled exception caught: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createBody(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
