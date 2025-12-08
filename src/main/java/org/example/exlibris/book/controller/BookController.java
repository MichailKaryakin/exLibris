package org.example.exlibris.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookRequest;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public BookResponse createBook(
            @RequestBody BookRequest request,
            Principal principal
    ) {
        return bookService.createBook(request, principal.getName());
    }

    @GetMapping("/get")
    public List<BookResponse> getUserBooks(Principal principal) {
        return bookService.getBooksForUser(principal.getName());
    }

    @GetMapping("/get-title/{title}")
    public List<BookResponse> getUserBooksByTitle(
            Principal principal,
            @PathVariable String title
    ) {
        return bookService.getBooksForUserByTitle(principal.getName(), title);
    }

    @GetMapping("/get-author/{author}")
    public List<BookResponse> getUserBooksByAuthor(
            Principal principal,
            @PathVariable String author
    ) {
        return bookService.getBooksForUserByAuthor(principal.getName(), author);
    }

    @PutMapping("/update/{bookId}")
    public BookResponse updateBook(
            @PathVariable Long bookId,
            @RequestBody BookRequest request,
            Principal principal
    ) {
        return bookService.updateBook(bookId, request, principal.getName());
    }

    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(
            @PathVariable Long bookId,
            Principal principal
    ) {
        bookService.deleteBook(bookId, principal.getName());
    }
}
