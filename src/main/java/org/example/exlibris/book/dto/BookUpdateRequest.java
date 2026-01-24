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

        @Min(1)
        Integer totalPages
) {
}
