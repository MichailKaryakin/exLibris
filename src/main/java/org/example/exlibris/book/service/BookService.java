package org.example.exlibris.book.service;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookRequest;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookResponse createBook(BookRequest request, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .year(request.year())
                .description(request.description())
                .user(user)
                .build();

        return toResponse(bookRepository.save(book));
    }

    public List<BookResponse> getBooksForUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow();

        return bookRepository.findAllByUserId(user.getId())
                .stream().map(this::toResponse).toList();
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getDescription()
        );
    }
}
