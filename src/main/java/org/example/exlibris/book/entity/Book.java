package org.example.exlibris.book.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "books",
        indexes = {
                @Index(name = "idx_books_user_id", columnList = "user_id"),
                @Index(name = "idx_books_user_title", columnList = "user_id,title"),
                @Index(name = "idx_books_user_author", columnList = "user_id,author")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private Integer year;

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<ReadingEntry> readingEntries = new ArrayList<>();
}
