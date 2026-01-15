package org.example.exlibris.book.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.exlibris.reading.entity.ReadingEntry;
import org.example.exlibris.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer year;
    private Integer totalPages;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<ReadingEntry> readingEntries = new ArrayList<>();
}
