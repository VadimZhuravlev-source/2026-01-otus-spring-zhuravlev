package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.BookCommentRepository;
import ru.otus.library_books.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookCommentServiceTest {

    @Mock
    private BookCommentRepository bookCommentRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookCommentServiceImpl bookCommentService;

    @Test
    @DisplayName("should find comments by book id")
    void shouldFindCommentsByBookId() {
        var book = createBook(1L);
        var comment1 = new BookComment(1L, "Great book", book);
        var comment2 = new BookComment(2L, "Masterpiece", book);
        when(bookCommentRepository.findByBookId(1L)).thenReturn(List.of(comment1, comment2));

        var comments = bookCommentService.findByBookId(1L);

        assertThat(comments).hasSize(2);
        assertThat(comments).containsExactly(comment1, comment2);
        verify(bookCommentRepository).findByBookId(1L);
    }

    @Test
    @DisplayName("should find comment by id")
    void shouldFindCommentById() {
        var book = createBook(1L);
        var comment = new BookComment(1L, "Great book", book);
        when(bookCommentRepository.findById(1L)).thenReturn(Optional.of(comment));

        var result = bookCommentService.findById(1L);

        assertThat(result).isEqualTo(comment);
        verify(bookCommentRepository).findById(1L);
    }

    @Test
    @DisplayName("should throw when comment not found by id")
    void shouldThrowWhenCommentNotFoundById() {
        when(bookCommentRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookCommentService.findById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment with id 100 not found");
        verify(bookCommentRepository).findById(100L);
    }

    @Test
    @DisplayName("should create comment")
    void shouldCreateComment() {
        var book = createBook(1L);
        var savedComment = new BookComment(5L, "New comment", book);
        when(bookRepository.getReferenceById(1L)).thenReturn(book);
        when(bookCommentRepository.save(any(BookComment.class))).thenReturn(savedComment);

        var result = bookCommentService.create(1L, "New comment");

        assertThat(result).isEqualTo(savedComment);
        assertThat(result.getText()).isEqualTo("New comment");
        assertThat(result.getBook()).isEqualTo(book);
        verify(bookRepository).getReferenceById(1L);
        verify(bookCommentRepository).save(any(BookComment.class));
    }

    @Test
    @DisplayName("should update comment text")
    void shouldUpdateCommentText() {
        var book = createBook(1L);
        var existingComment = new BookComment(1L, "Old text", book);
        when(bookCommentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        when(bookCommentRepository.save(any(BookComment.class))).thenReturn(existingComment);

        var result = bookCommentService.update(1L, "Updated text");

        assertThat(result).isEqualTo(existingComment);
        assertThat(result.getText()).isEqualTo("Updated text");
        verify(bookCommentRepository).findById(1L);
        verify(bookCommentRepository).save(any(BookComment.class));
    }

    @Test
    @DisplayName("should delete comment by id")
    void shouldDeleteCommentById() {
        var book = createBook(1L);
        var comment = new BookComment(1L, "Some text", book);
        when(bookCommentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThatCode(() -> bookCommentService.deleteById(1L)).doesNotThrowAnyException();

        verify(bookCommentRepository).findById(1L);
        verify(bookCommentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("should throw when deleting non-existent comment")
    void shouldThrowWhenDeletingNonExistentComment() {
        when(bookCommentRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookCommentService.deleteById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment with id 100 not found");
        verify(bookCommentRepository).findById(100L);
    }

    private Book createBook(long id) {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        return new Book(id, "Crime and Punishment", author, genre, null);
    }
}
