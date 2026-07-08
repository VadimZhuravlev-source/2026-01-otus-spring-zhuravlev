package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;

@DataJpaTest
class BookCommentRepositoryTest {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("should find comments by book id")
    void shouldFindCommentsByBookId() {
        var comments = bookCommentRepository.findByBookId(1L);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getText()).isEqualTo("Strong psychological novel");
    }

    @Test
    @DisplayName("should create and update comment")
    void shouldCreateAndUpdateComment() {
        var book = bookRepository.getReferenceById(1L);
        var comment = bookCommentRepository.save(new BookComment(0, "New comment", book));

        assertThat(comment.getId()).isPositive();
        assertThat(comment.getText()).isEqualTo("New comment");

        comment.setText("Updated comment");
        var updatedComment = bookCommentRepository.save(comment);

        assertThat(updatedComment.getText()).isEqualTo("Updated comment");
    }

    @Test
    @DisplayName("should return empty list when book has no comments")
    void shouldReturnEmptyListWhenBookHasNoComments() {
        var authorRef = authorRepository.getReferenceById(1L);
        var genreRef = genreRepository.getReferenceById(1L);
        var createdBook = bookRepository.save(new Book(0, "Book without comments", authorRef, genreRef, null));

        var comments = bookCommentRepository.findByBookId(createdBook.getId());

        assertThat(comments).isEmpty();
    }

    @Test
    @DisplayName("should find seeded comment by id")
    void shouldFindSeededCommentById() {
        var comment = bookCommentRepository.findById(1L);

        assertThat(comment).isPresent();
        assertThat(comment.get().getText()).isEqualTo("Strong psychological novel");
    }
}
