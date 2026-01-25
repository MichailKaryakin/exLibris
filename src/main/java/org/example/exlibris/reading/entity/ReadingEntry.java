package org.example.exlibris.reading.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.exlibris.reading.enums.ReadingStatus;
import org.example.exlibris.user.entity.User;
import org.example.exlibris.book.entity.Book;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reading",
        indexes = {
                @Index(name = "idx_reading_user_id", columnList = "user_id"),
                @Index(name = "idx_reading_user_status", columnList = "user_id,status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status;

    private Integer score;

    @Column(name = "current_page")
    private Integer currentPage;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(length = 2000)
    private String notes;
}
