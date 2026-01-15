package org.example.exlibris.reading.controller;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.reading.dto.ReadingResponse;
import org.example.exlibris.reading.service.ReadingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService service;

    @GetMapping("/list")
    public List<ReadingResponse> list(Authentication auth) {
        return service.getUserReading(auth.getName());
    }

    @GetMapping("/list-now-reading")
    public List<ReadingResponse> getCurrentReading(Authentication auth) {
        return service.getCurrentReading(auth.getName());
    }

    @GetMapping("/reading-history")
    public List<ReadingResponse> getReadingHistory(Authentication auth) {
        return service.getReadingHistory(auth.getName());
    }

    @PostMapping("/start/{bookId}")
    public ReadingResponse start(
            Authentication auth,
            @PathVariable Long bookId
    ) {
        return service.startReading(auth.getName(), bookId);
    }

    @PostMapping("/finish/{readingId}")
    public ReadingResponse finish(
            @PathVariable Long readingId,
            @RequestParam Integer score,
            @RequestParam(required = false) String notes
    ) {
        return service.finishReading(readingId, score, notes);
    }

    @PutMapping("/update-progress/{readingId}")
    public ReadingResponse updateProgress(
            @PathVariable Long readingId,
            @RequestParam Integer pageNumber
    ) {
        return service.updateProgress(readingId, pageNumber);
    }
}
