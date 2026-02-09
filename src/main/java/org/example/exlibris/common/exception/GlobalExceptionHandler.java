package org.example.exlibris.common.exception;

import org.example.exlibris.book.exception.AccessDeniedBookOperationException;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.common.dto.ErrorResponse;
import org.example.exlibris.reading.exception.ReadingNotFoundException;
import org.example.exlibris.reading.exception.ReadingStateException;
import org.example.exlibris.user.exception.EmailAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse body(int status, String error, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message
        );
    }

    // ========== AUTH / USER ==========

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyUsedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body(409, "EMAIL_ALREADY_USED", e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body(401, "BAD_CREDENTIALS", "Invalid email or password"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body(404, "USER_NOT_FOUND", e.getMessage()));
    }

    // ========== BOOK ==========

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body(404, "BOOK_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedBookOperationException.class)
    public ResponseEntity<ErrorResponse> handleBookAccessDenied(
            AccessDeniedBookOperationException e
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body(403, "BOOK_ACCESS_DENIED", e.getMessage()));
    }

    // ========== READING ==========

    @ExceptionHandler(ReadingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadingNotFound(
            ReadingNotFoundException e
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body(404, "READING_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(ReadingStateException.class)
    public ResponseEntity<ErrorResponse> handleReadingState(
            ReadingStateException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body(409, "READING_STATE_ERROR", e.getMessage()));
    }

    // ========== VALIDATION ==========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e
    ) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(body(400, "VALIDATION_ERROR", msg));
    }

    // ========== FALLBACK ==========

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> fallback() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body(500, "INTERNAL_ERROR", "Internal server error"));
    }
}
