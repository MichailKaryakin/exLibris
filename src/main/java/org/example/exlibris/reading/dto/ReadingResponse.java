package org.example.exlibris.reading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.reading.enums.ReadingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReadingResponse {
    private Long id;
    private BookResponse book;
    private ReadingStatus status;
    private Integer score;
    private LocalDateTime finishedAt;
    private String notes;
}
