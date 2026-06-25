package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.GenreRepository;

class GenreServiceTest {

    private GenreRepository genreRepository;

    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genreRepository = mock(GenreRepository.class);
        genreService = new GenreService(genreRepository);
    }

    @Test
    @DisplayName("should create genre")
    void shouldCreateGenre() {
        when(genreRepository.insert("Drama")).thenReturn(new Genre(4, "Drama"));

        var genre = genreService.create("Drama");

        assertThat(genre.getId()).isEqualTo(4);
        assertThat(genre.getName()).isEqualTo("Drama");
        verify(genreRepository).insert("Drama");
    }
}
