package org.example.exlibris.book.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookCreateRequest;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.dto.BookUpdateRequest;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.book.repository.BookSpecifications;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private String normalizeIsbn(String isbn) {
        if (isbn == null) return null;
        return isbn.replaceAll("-", "").trim();
    }

    public BookResponse create(BookCreateRequest request, String username) {
        User user = getUser(username);

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .year(request.year())
                .totalPages(request.totalPages())
                .description(request.description())
                .isbn(normalizeIsbn(request.isbn()))
                .series(request.series())
                .user(user)
                .build();

        return toResponse(bookRepository.save(book));
    }

    public Page<BookResponse> getAll(
            String username,
            String title,
            String author,
            Pageable pageable
    ) {
        User user = getUser(username);

        Specification<Book> spec = Specification.where(BookSpecifications.hasUserId(user.getId()))
                .and(BookSpecifications.titleContains(title))
                .and(BookSpecifications.authorContains(author));

        return bookRepository.findAll(spec, pageable)
                .map(this::toResponse);
    }

    public BookResponse getById(Long bookId, String username) {
        User user = getUser(username);

        return bookRepository.findByIdAndUserId(bookId, user.getId())
                .map(this::toResponse)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));
    }

    public BookResponse update(
            Long bookId,
            BookUpdateRequest request,
            String username
    ) {
        User user = getUser(username);

        Book book = bookRepository.findByIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (request.title() != null) book.setTitle(request.title());
        if (request.author() != null) book.setAuthor(request.author());
        if (request.year() != null) book.setYear(request.year());
        if (request.totalPages() != null) book.setTotalPages(request.totalPages());
        if (request.description() != null) book.setDescription(request.description());
        if (request.isbn() != null) book.setIsbn(normalizeIsbn(request.isbn()));
        if (request.series() != null) book.setSeries(request.series());

        return toResponse(book);
    }

    public void delete(Long bookId, String username) {
        User user = getUser(username);

        Book book = bookRepository.findByIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        bookRepository.delete(book);
    }

    private User getUser(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getTotalPages(),
                book.getDescription(),
                book.getIsbn(),
                book.getSeries()
        );
    }
}
