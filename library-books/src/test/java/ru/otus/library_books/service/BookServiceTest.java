package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.AuthorRepository;
import ru.otus.library_books.repository.BookRepository;
import ru.otus.library_books.repository.GenreRepository;

class BookServiceTest {

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    private GenreRepository genreRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        authorRepository = mock(AuthorRepository.class);
        genreRepository = mock(GenreRepository.class);
        var authorService = new AuthorService(authorRepository);
        var genreService = new GenreService(genreRepository);
        bookService = new BookService(bookRepository, authorService, genreService);
    }

    @Test
    @DisplayName("should validate author and genre before creating book")
    void shouldValidateAuthorAndGenreBeforeCreatingBook() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(new Author(1, "Author")));
        when(genreRepository.findById(2)).thenReturn(Optional.of(new Genre(2, "Genre")));

        bookService.create("Title", 1, 2);

        verify(authorRepository).findById(1);
        verify(genreRepository).findById(2);
        verify(bookRepository).insert("Title", 1, 2);
    }

    @Test
    @DisplayName("should throw exception when author does not exist")
    void shouldThrowExceptionWhenAuthorDoesNotExist() {
        when(authorRepository.findById(100)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.create("Title", 100, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id 100 not found");
    }
}
