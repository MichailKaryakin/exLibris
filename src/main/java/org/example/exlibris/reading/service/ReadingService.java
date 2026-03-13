package org.example.exlibris.reading.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.exception.BookNotFoundException;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.reading.dto.*;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.exception.ReadingNotFoundException;
import org.example.exlibris.reading.exception.ReadingStateException;
import org.example.exlibris.reading.repository.ReadingRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (readingRepo.existsByUserIdAndBookIdAndStatus(
                user.getId(),
                book.getId(),
                ReadingStatus.ABANDONED
        )) {
            throw new ReadingStateException("Book had already been read and abandoned");
        }

        if (readingRepo.existsByUserIdAndBookIdAndStatus(
                user.getId(),
                book.getId(),
                ReadingStatus.FINISHED
        )) {
            throw new ReadingStateException("Book was already finished");
        }

        ReadingEntry entry = ReadingEntry.builder()
                .user(user)
                .book(book)
                .status(ReadingStatus.READING)
                .currentPage(0)
                .build();

        return toResponse(readingRepo.save(entry));
    }

    public ReadingStatsResponse getUserStats(String email) {
        User user = getUser(email);
        List<ReadingEntry> entries = readingRepo.findAllByUserId(user.getId());

        long inProgress = 0;
        long finished = 0;
        long abandoned = 0;
        int totalPages = 0;
        double sumScore = 0;
        int scoredBooksCount = 0;

        for (ReadingEntry entry : entries) {
            totalPages += entry.getCurrentPage();

            switch (entry.getStatus()) {
                case READING -> inProgress++;
                case FINISHED -> {
                    finished++;
                    if (entry.getScore() != null && entry.getScore() > 0) {
                        sumScore += entry.getScore();
                        scoredBooksCount++;
                    }
                }
                case ABANDONED -> abandoned++;
            }
        }

        double avgScore = scoredBooksCount > 0 ? Math.round((sumScore / scoredBooksCount) * 10.0) / 10.0 : 0.0;

        return new ReadingStatsResponse(
                inProgress,
                finished,
                abandoned,
                totalPages,
                avgScore
        );
    }

    public Page<ReadingResponse> getAll(
            String email,
            ReadingStatus status,
            String query,
            Pageable pageable
    ) {
        User user = getUser(email);
        Specification<ReadingEntry> spec = createSpecification(user.getId(), status, query);

        return readingRepo.findAll(spec, pageable)
                .map(this::toResponse);
    }

    public ReadingResponse getById(Long id, String email) {
        User user = getUser(email);
        ReadingEntry entry = readingRepo.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ReadingNotFoundException("Reading entry not found"));
        return toResponse(entry);
    }

    public void delete(Long id, String email) {
        User user = getUser(email);
        ReadingEntry entry = readingRepo.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ReadingNotFoundException("Reading entry not found"));

        readingRepo.delete(entry);
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

    private Specification<ReadingEntry> createSpecification(
            Long userId,
            ReadingStatus status,
            String query
    ) {
        return (root, cq, cb) -> {
            if (cq.getResultType() != Long.class && cq.getResultType() != long.class) {
                root.fetch("book");
            }

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.toLowerCase() + "%";

                var bookJoin = root.get("book");

                Predicate titleMatch = cb.like(cb.lower(bookJoin.get("title")), pattern);
                Predicate authorMatch = cb.like(cb.lower(bookJoin.get("author")), pattern);

                predicates.add(cb.or(titleMatch, authorMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
