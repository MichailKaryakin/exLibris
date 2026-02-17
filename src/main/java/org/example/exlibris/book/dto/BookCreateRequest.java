package org.example.exlibris.book.dto;

import jakarta.validation.constraints.*;

public record BookCreateRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
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

        @NotNull
        @Min(1)
        Integer totalPages
) {
}
