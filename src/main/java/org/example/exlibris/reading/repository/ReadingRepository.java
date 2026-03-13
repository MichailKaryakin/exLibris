package org.example.exlibris.reading.repository;

import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<ReadingEntry, Long>, JpaSpecificationExecutor<ReadingEntry> {

    Optional<ReadingEntry> findByIdAndUserId(Long id, Long userId);

    List<ReadingEntry> findAllByUserId(Long userId);

    boolean existsByUserIdAndBookIdAndStatus(
            Long userId,
            Long bookId,
            ReadingStatus status
    );
}
