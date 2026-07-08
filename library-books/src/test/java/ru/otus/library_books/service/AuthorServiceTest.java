package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import ru.otus.library_books.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    @DisplayName("should find all authors")
    void shouldFindAllAuthors() {
        var author1 = new Author(1L, "Fyodor Dostoevsky");
        var author2 = new Author(2L, "Jules Verne");
        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        var authors = authorService.findAll();

        assertThat(authors).hasSize(2);
        assertThat(authors).containsExactly(author1, author2);
        verify(authorRepository).findAll();
    }

    @Test
    @DisplayName("should find author by id")
    void shouldFindAuthorById() {
        var author = new Author(1L, "Fyodor Dostoevsky");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        var result = authorService.findById(1L);

        assertThat(result).isEqualTo(author);
        verify(authorRepository).findById(1L);
    }

    @Test
    @DisplayName("should throw when author not found by id")
    void shouldThrowWhenAuthorNotFoundById() {
        when(authorRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id 100 not found");
        verify(authorRepository).findById(100L);
    }

    @Test
    @DisplayName("should create author")
    void shouldCreateAuthor() {
        var savedAuthor = new Author(5L, "Leo Tolstoy");
        when(authorRepository.insert("Leo Tolstoy")).thenReturn(savedAuthor);

        var result = authorService.create("Leo Tolstoy");

        assertThat(result).isEqualTo(savedAuthor);
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getFullName()).isEqualTo("Leo Tolstoy");
        verify(authorRepository).insert("Leo Tolstoy");
    }
}
