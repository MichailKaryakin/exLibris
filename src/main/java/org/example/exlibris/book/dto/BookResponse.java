package org.example.exlibris.book.dto;

public record BookResponse(
        Long id,
        String title,
        String author,
        Integer year,
        String description
) {
}
