package org.example.exlibris.reading.dto;

import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.reading.enums.ReadingStatus;

import java.time.LocalDateTime;

public record ReadingResponse(
        Long id,
        BookResponse book,
        ReadingStatus status,
        Integer score,
        Integer currentPage,
        LocalDateTime finishedAt,
        String notes
) {
}
