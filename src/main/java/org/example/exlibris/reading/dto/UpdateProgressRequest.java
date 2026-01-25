package org.example.exlibris.reading.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProgressRequest(
        @NotNull
        @Min(0)
        Integer currentPage
) {
}
