package org.example.exlibris.book.repository;

import org.example.exlibris.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByUserId(Long userId, Pageable pageable);

    Page<Book> findAllByUserIdAndTitleContainingIgnoreCase(
            Long userId,
            String title,
            Pageable pageable
    );

    Page<Book> findAllByUserIdAndAuthorContainingIgnoreCase(
            Long userId,
            String author,
            Pageable pageable
    );

    Optional<Book> findByIdAndUserId(Long bookId, Long userId);
}
