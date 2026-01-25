package org.example.exlibris.reading.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.reading.dto.FinishReadingRequest;
import org.example.exlibris.reading.dto.ReadingResponse;
import org.example.exlibris.reading.dto.StartReadingRequest;
import org.example.exlibris.reading.dto.UpdateProgressRequest;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.service.ReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
@Validated
public class ReadingController {

    private final ReadingService service;

    @GetMapping
    public List<ReadingResponse> getAll(
            @RequestParam(required = false) ReadingStatus status,
            Principal principal
    ) {
        return service.getAll(principal.getName(), status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadingResponse start(
            @Valid @RequestBody StartReadingRequest request,
            Principal principal
    ) {
        return service.start(request, principal.getName());
    }

    @PatchMapping("/{id}")
    public ReadingResponse updateProgress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProgressRequest request,
            Principal principal
    ) {
        return service.updateProgress(id, request, principal.getName());
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
