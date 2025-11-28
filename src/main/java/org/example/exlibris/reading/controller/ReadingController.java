package org.example.exlibris.reading.controller;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.service.ReadingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService service;

    @PostMapping("/start/{bookId}")
    public ReadingEntry start(Authentication auth, @PathVariable Long bookId) {
        return service.startReading(auth.getName(), bookId);
    }

    @PostMapping("/finish/{readingId}")
    public ReadingEntry finish(
            @PathVariable Long readingId,
            @RequestParam Integer score,
            @RequestParam(required = false) String notes
    ) {
        return service.finishReading(readingId, score, notes);
    }

    @GetMapping("/list")
    public List<ReadingEntry> list(Authentication auth) {
        return service.getUserReading(auth.getName());
    }
}
