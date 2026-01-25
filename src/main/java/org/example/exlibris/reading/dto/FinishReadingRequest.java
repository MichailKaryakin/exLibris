package org.example.exlibris.reading.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FinishReadingRequest(
        @NotNull
        @Min(1)
        @Max(10)
        Integer score,

        @Size(max = 2000)
        String notes
) {
}
