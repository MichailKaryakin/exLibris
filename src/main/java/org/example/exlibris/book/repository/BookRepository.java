package org.example.exlibris.book.repository;

import lombok.NonNull;
import org.example.exlibris.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<@NonNull Book, @NonNull Long> {
    List<Book> findAllByUserId(Long userId);
}
