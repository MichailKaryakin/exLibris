package org.example.exlibris.book.service;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookRequest;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.exception.AccessDeniedBookOperationException;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookResponse createBook(BookRequest request, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return bookRepository.findAllByUserId(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public List<BookResponse> getBooksForUserByTitle(String username, String title) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return bookRepository.findAllByUserIdAndTitle(user.getId(), title)
                .stream().map(this::toResponse).toList();
    }

    public List<BookResponse> getBooksForUserByAuthor(String username, String author) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return bookRepository.findAllByUserIdAndAuthor(user.getId(), author)
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

    public BookResponse updateBook(Long bookId, BookRequest request, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Book book = bookRepository.findByIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedBookOperationException("You cannot update this book");
        }

        if (request.title() != null) book.setTitle(request.title());
        if (request.description() != null) book.setDescription(request.description());
        if (request.year() != null) book.setYear(request.year());
        if (request.author() != null) book.setAuthor(request.author());

        return toResponse(bookRepository.save(book));
    }

    public void deleteBook(Long bookId, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedBookOperationException("You cannot delete this book");
        }

        bookRepository.delete(book);
    }
}
