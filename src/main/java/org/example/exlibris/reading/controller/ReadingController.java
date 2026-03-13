package org.example.exlibris.reading.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.reading.dto.*;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.service.ReadingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
@Validated
public class ReadingController {

    private final ReadingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadingResponse start(
            @Valid @RequestBody StartReadingRequest request,
            Principal principal
    ) {
        return service.start(request, principal.getName());
    }

    @GetMapping("/stats")
    public ReadingStatsResponse getStats(Principal principal) {
        return service.getUserStats(principal.getName());
    }

    @GetMapping
    public Page<ReadingResponse> getAll(
            @RequestParam(required = false) ReadingStatus status,
            @RequestParam(required = false) String query,
            Pageable pageable,
            Principal principal
    ) {
        return service.getAll(principal.getName(), status, query, pageable);
    }

    @GetMapping("/{id}")
    public ReadingResponse getById(@PathVariable Long id, Principal principal) {
        return service.getById(id, principal.getName());
    }

    @PatchMapping("/{id}")
    public ReadingResponse updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProgressRequest request,
            Principal principal
    ) {
        return service.updateProgress(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal.getName());
    }

    @PostMapping("/{id}/finish")
    public ReadingResponse finish(
            @PathVariable Long id,
            @Valid @RequestBody FinishReadingRequest request,
            Principal principal
    ) {
        return service.finish(id, request, principal.getName());
    }
}
