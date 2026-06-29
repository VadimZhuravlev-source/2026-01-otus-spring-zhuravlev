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
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorServiceImpl authorService;

    @Mock
    private GenreServiceImpl genreService;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("should find all books")
    void shouldFindAllBooks() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var book1 = new Book(1L, "Crime and Punishment", author, genre);
        var book2 = new Book(2L, "The Idiot", author, genre);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        var books = bookService.findAll();

        assertThat(books).hasSize(2);
        assertThat(books).containsExactly(book1, book2);
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("should find book by id")
    void shouldFindBookById() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var book = new Book(1L, "Crime and Punishment", author, genre);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        var result = bookService.findById(1L);

        assertThat(result).isEqualTo(book);
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("should throw when book not found by id")
    void shouldThrowWhenBookNotFoundById() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id 100 not found");
        verify(bookRepository).findById(100L);
    }

    @Test
    @DisplayName("should create book")
    void shouldCreateBook() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var savedBook = new Book(4L, "The Gambler", author, genre);

        when(authorService.findById(1L)).thenReturn(author);
        when(genreService.findById(1L)).thenReturn(genre);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        var result = bookService.create("The Gambler", 1L, 1L);

        assertThat(result).isEqualTo(savedBook);
        assertThat(result.getTitle()).isEqualTo("The Gambler");
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getGenre()).isEqualTo(genre);
        verify(authorService).findById(1L);
        verify(genreService).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("should update book")
    void shouldUpdateBook() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var existingBook = new Book(1L, "Old Title", author, genre);
        var newAuthor = new Author(2L, "Jules Verne");
        var newGenre = new Genre(2L, "Adventure");
        var updatedBook = new Book(1L, "New Title", newAuthor, newGenre);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorService.findById(2L)).thenReturn(newAuthor);
        when(genreService.findById(2L)).thenReturn(newGenre);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        var result = bookService.update(1L, "New Title", 2L, 2L);

        assertThat(result).isEqualTo(updatedBook);
        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getAuthor()).isEqualTo(newAuthor);
        assertThat(result.getGenre()).isEqualTo(newGenre);
        verify(bookRepository).findById(1L);
        verify(authorService).findById(2L);
        verify(genreService).findById(2L);
        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("should delete book by id")
    void shouldDeleteBookById() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var book = new Book(1L, "Crime and Punishment", author, genre);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThatCode(() -> bookService.deleteById(1L)).doesNotThrowAnyException();

        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("should throw when deleting non-existent book")
    void shouldThrowWhenDeletingNonExistentBook() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id 100 not found");
        verify(bookRepository).findById(100L);
    }
}
