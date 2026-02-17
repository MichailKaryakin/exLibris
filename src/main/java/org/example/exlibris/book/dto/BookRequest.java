package org.example.exlibris.book.dto;

import jakarta.validation.constraints.*;

public record BookRequest(

        @Size(max = 255)
        String title,

        @Size(max = 255)
        String author,

        @Size(max = 255)
        String series
) {
}
