package org.example.exlibris.reading.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.reading.dto.FinishReadingRequest;
import org.example.exlibris.reading.dto.ReadingResponse;
import org.example.exlibris.reading.dto.StartReadingRequest;
import org.example.exlibris.reading.dto.UpdateProgressRequest;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.exception.ReadingNotFoundException;
import org.example.exlibris.reading.exception.ReadingStateException;
import org.example.exlibris.reading.repository.ReadingRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingService {

    private final ReadingRepository readingRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    public ReadingResponse start(StartReadingRequest request, String email) {
        User user = getUser(email);

        Book book = bookRepo.findByIdAndUserId(request.bookId(), user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (readingRepo.existsByUserIdAndBookIdAndStatus(
                user.getId(),
                book.getId(),
                ReadingStatus.READING
        )) {
            throw new ReadingStateException("Book is already being read");
        }

        ReadingEntry entry = ReadingEntry.builder()
                .user(user)
                .book(book)
                .status(ReadingStatus.READING)
                .currentPage(0)
                .build();

        return toResponse(readingRepo.save(entry));
    }

    public List<ReadingResponse> getAll(String email, ReadingStatus status) {
        User user = getUser(email);

        List<ReadingEntry> entries = (status == null)
                ? readingRepo.findAllByUserId(user.getId())
                : readingRepo.findAllByUserIdAndStatus(user.getId(), status);

        return entries.stream()
                .map(this::toResponse)
                .toList();
    }

    public ReadingResponse updateProgress(
            Long readingId,
            UpdateProgressRequest request,
            String email
    ) {
        User user = getUser(email);

        ReadingEntry entry = readingRepo.findByIdAndUserId(readingId, user.getId())
                .orElseThrow(() -> new ReadingNotFoundException("Reading entry not found"));

        if (entry.getStatus() != ReadingStatus.READING) {
            throw new ReadingStateException("Cannot update progress for finished reading");
        }

        int maxPages = entry.getBook().getTotalPages();

        if (request.currentPage() < 0) {
            throw new ReadingStateException("Current page cannot be negative");
        }

        if (maxPages > 0 && request.currentPage() > maxPages) {
            throw new ReadingStateException(
                    "Current page cannot exceed total pages of the book"
            );
        }

        entry.setCurrentPage(request.currentPage());
        return toResponse(entry);
    }

    public ReadingResponse finish(
            Long readingId,
            FinishReadingRequest request,
            String email
    ) {
        User user = getUser(email);

        ReadingEntry entry = readingRepo.findByIdAndUserId(readingId, user.getId())
                .orElseThrow(() -> new ReadingNotFoundException("Reading entry not found"));

        if (entry.getStatus() == ReadingStatus.FINISHED) {
            throw new ReadingStateException("Reading already finished");
        }

        entry.setStatus(request.abandon() == true ? ReadingStatus.ABANDONED : ReadingStatus.FINISHED);
        entry.setScore(request.score());
        entry.setNotes(request.notes());
        entry.setFinishedAt(LocalDateTime.now());

        return toResponse(entry);
    }

    private User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private BookResponse toBookResponse(Book book) {
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

    private ReadingResponse toResponse(ReadingEntry entry) {
        Book book = entry.getBook();

        double percentage = 0.0;
        if (book.getTotalPages() > 0) {
            percentage = (double) entry.getCurrentPage() / book.getTotalPages() * 100;
            percentage = Math.round(percentage * 10.0) / 10.0;
        }

        return new ReadingResponse(
                entry.getId(),
                toBookResponse(book),
                entry.getStatus(),
                entry.getScore(),
                entry.getCurrentPage(),
                percentage,
                entry.getFinishedAt(),
                entry.getNotes()
        );
    }
}
