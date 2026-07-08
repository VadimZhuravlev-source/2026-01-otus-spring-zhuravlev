package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JPQLBookCommentRepository.class, JPQLBookRepository.class, JPQLAuthorRepository.class, JPQLGenreRepository.class})
class BookCommentRepositoryTest {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Autowired
    private BookRepository bookRepository;

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
        var comment = bookCommentRepository.insert("New comment", 1L);

        assertThat(comment.getId()).isPositive();
        assertThat(comment.getText()).isEqualTo("New comment");

        var updatedComment = bookCommentRepository.update(comment.getId(), "Updated comment", 1L);

        assertThat(updatedComment.getText()).isEqualTo("Updated comment");
    }

    @Test
    @DisplayName("should return empty list when book has no comments")
    void shouldReturnEmptyListWhenBookHasNoComments() {
        var createdBook = bookRepository.insert("Book without comments", 1L, 1L);

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