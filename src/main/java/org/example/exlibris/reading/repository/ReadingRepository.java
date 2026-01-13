package org.example.exlibris.reading.repository;

import lombok.NonNull;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingRepository extends JpaRepository<@NonNull ReadingEntry, @NonNull Long> {
    List<ReadingEntry> findAllByUserEmail(String email);

    List<ReadingEntry> findAllByUserEmailAndStatus(String email, ReadingStatus status);
}
