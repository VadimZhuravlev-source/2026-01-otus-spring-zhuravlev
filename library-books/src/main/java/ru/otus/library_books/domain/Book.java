package ru.otus.library_books.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Getter
@Setter
@ToString(exclude = "bookComments")
@EqualsAndHashCode(exclude = "bookComments")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "Book.comments",
        attributeNodes = @NamedAttributeNode("bookComments"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 5)
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<BookComment> bookComments;

}
