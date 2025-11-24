package org.example.exlibris.book.dto;

public record BookRequest(
        String title,
        String author,
        Integer year,
        String description
) {
}
