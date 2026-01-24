package org.example.exlibris.reading.service;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.dto.BookResponse;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.reading.dto.ReadingResponse;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.repository.ReadingRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    private ReadingResponse mapToResponse(ReadingEntry entry) {
        Book book = entry.getBook();
        BookResponse bookResponse = new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getTotalPages(),
                book.getDescription()
        );

        return new ReadingResponse(
                entry.getId(),
                bookResponse,
                entry.getStatus(),
                entry.getScore(),
                entry.getFinishedAt(),
                entry.getNotes(),
                entry.getCurrentPage()
        );
    }

    public ReadingResponse startReading(String email, Long bookId) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        ReadingEntry entry = ReadingEntry.builder()
                .user(user)
                .book(book)
                .status(ReadingStatus.READING)
                .build();

        readingRepo.save(entry);
        return mapToResponse(entry);
    }

    public ReadingResponse finishReading(Long readingId, Integer score, String notes) {
        ReadingEntry entry = readingRepo.findById(readingId).orElseThrow();
        entry.setStatus(ReadingStatus.FINISHED);
        entry.setScore(score);
        entry.setNotes(notes);
        entry.setFinishedAt(LocalDateTime.now());

        readingRepo.save(entry);

        return mapToResponse(entry);
    }

    public List<ReadingResponse> getUserReading(String email) {
        List<ReadingEntry> entryList = readingRepo.findAllByUserEmail(email);
        return entryList.stream().map(this::mapToResponse).toList();
    }

    public List<ReadingResponse> getCurrentReading(String email) {
        List<ReadingEntry> entryList = readingRepo.findAllByUserEmailAndStatus(email, ReadingStatus.READING);
        return entryList.stream().map(this::mapToResponse).toList();
    }

    public ReadingResponse updateProgress(Long readingId, Integer pageNumber) {
        ReadingEntry entry = readingRepo.findById(readingId).orElseThrow();
        entry.setCurrentPage(pageNumber);

        readingRepo.save(entry);

        return mapToResponse(entry);
    }

    public List<ReadingResponse> getReadingHistory(String email) {
        List<ReadingEntry> entryList = readingRepo.findAllByUserEmail(email);
        return entryList.stream()
                .map(this::mapToResponse)
                .sorted(Comparator.comparing(ReadingResponse::getFinishedAt))
                .toList();
    }
}
