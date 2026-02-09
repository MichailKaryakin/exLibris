package org.example.exlibris.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookCreateRequest;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.dto.BookUpdateRequest;
import org.example.exlibris.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(
            @Valid @RequestBody BookCreateRequest request,
            Principal principal
    ) {
        return bookService.create(request, principal.getName());
    }

    @GetMapping
    public Page<BookResponse> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            Pageable pageable,
            Principal principal
    ) {
        return bookService.getAll(
                principal.getName(),
                title,
                author,
                pageable
        );
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id, Principal principal) {
        return bookService.getById(id, principal.getName());
    }

    @PutMapping("/{id}")
    public BookResponse update(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request,
            Principal principal
    ) {
        return bookService.update(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            Principal principal
    ) {
        bookService.delete(id, principal.getName());
    }
}
