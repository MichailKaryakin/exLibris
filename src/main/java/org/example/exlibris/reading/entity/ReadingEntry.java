package org.example.exlibris.reading.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.book.entity.Book;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reading")
public class ReadingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    private Integer score;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    private String notes;

    private Integer currentPage;
}
