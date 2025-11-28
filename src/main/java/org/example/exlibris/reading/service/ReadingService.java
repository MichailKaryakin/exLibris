package org.example.exlibris.reading.service;

import lombok.RequiredArgsConstructor;
import org.example.exlibris.book.entity.Book;
import org.example.exlibris.book.repository.BookRepository;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.reading.repository.ReadingRepository;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    public ReadingEntry startReading(String email, Long bookId) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        ReadingEntry reading = ReadingEntry.builder()
                .user(user)
                .book(book)
                .status(ReadingStatus.READING)
                .build();

        return readingRepo.save(reading);
    }

    public ReadingEntry finishReading(Long readingId, Integer score, String notes) {
        ReadingEntry r = readingRepo.findById(readingId).orElseThrow();
        r.setStatus(ReadingStatus.FINISHED);
        r.setScore(score);
        r.setNotes(notes);
        r.setFinishedAt(LocalDateTime.now());
        return readingRepo.save(r);
    }

    public List<ReadingEntry> getUserReading(String email) {
        return readingRepo.findAllByUserEmail(email);
    }
}
