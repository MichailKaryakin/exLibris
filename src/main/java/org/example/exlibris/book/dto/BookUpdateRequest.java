package org.example.exlibris.book.dto;

import jakarta.validation.constraints.*;

public record BookUpdateRequest(

        @Size(max = 255)
        String title,

        @Size(max = 255)
        String author,

        @Min(0)
        @Max(2100)
        Integer year,

        @Size(max = 2000)
        String description,

        @Pattern(
                regexp = "^(97[89])?\\d{9}(\\d|X)$",
                message = "Invalid ISBN format"
        )
        @Size(max = 20)
        String isbn,

        @Size(max = 255)
        String series,

        @Min(1)
        Integer totalPages
) {
}
