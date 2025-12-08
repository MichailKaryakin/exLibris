package org.example.exlibris.book.repository;

import lombok.NonNull;
import org.example.exlibris.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<@NonNull Book, @NonNull Long> {
    List<Book> findAllByUserId(Long userId);

    List<Book> findAllByUserIdAndTitle(Long userId, String title);

    List<Book> findAllByUserIdAndAuthor(Long userId, String author);

    Optional<Book> findByIdAndUserId(Long bookId, Long userId);
}
