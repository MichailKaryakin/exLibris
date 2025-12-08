package org.example.exlibris.book.exception;

public class AccessDeniedBookOperationException extends RuntimeException {
    public AccessDeniedBookOperationException(String message) {
        super(message);
    }
}
