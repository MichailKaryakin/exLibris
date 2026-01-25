package org.example.exlibris.reading.dto;

import jakarta.validation.constraints.NotNull;

public record StartReadingRequest(
        @NotNull
        Long bookId
) {
}
