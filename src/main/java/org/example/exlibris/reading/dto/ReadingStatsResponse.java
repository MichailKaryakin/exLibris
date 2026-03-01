package org.example.exlibris.reading.dto;

public record ReadingStatsResponse(
        long totalBooksInProgress,
        long totalBooksFinished,
        long totalBooksAbandoned,
        int totalPagesRead,
        double averageScore
) {
}
