package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.repository.AuthorRepository;

class AuthorServiceTest {

    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepository);
    }

    @Test
    @DisplayName("should create author")
    void shouldCreateAuthor() {
        when(authorRepository.insert("Leo Tolstoy")).thenReturn(new Author(4, "Leo Tolstoy"));

        var author = authorService.create("Leo Tolstoy");

        assertThat(author.id()).isEqualTo(4);
        assertThat(author.fullName()).isEqualTo("Leo Tolstoy");
        verify(authorRepository).insert("Leo Tolstoy");
    }
}
