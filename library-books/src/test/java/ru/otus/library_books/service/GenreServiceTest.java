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

import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.GenreRepository;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    @DisplayName("should find all genres")
    void shouldFindAllGenres() {
        var genre1 = new Genre(1L, "Psychological Fiction");
        var genre2 = new Genre(2L, "Adventure");
        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2));

        var genres = genreService.findAll();

        assertThat(genres).hasSize(2);
        assertThat(genres).containsExactly(genre1, genre2);
        verify(genreRepository).findAll();
    }

    @Test
    @DisplayName("should find genre by id")
    void shouldFindGenreById() {
        var genre = new Genre(1L, "Psychological Fiction");
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        var result = genreService.findById(1L);

        assertThat(result).isEqualTo(genre);
        verify(genreRepository).findById(1L);
    }

    @Test
    @DisplayName("should throw when genre not found by id")
    void shouldThrowWhenGenreNotFoundById() {
        when(genreRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findById(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre with id 100 not found");
        verify(genreRepository).findById(100L);
    }

    @Test
    @DisplayName("should create genre")
    void shouldCreateGenre() {
        var savedGenre = new Genre(5L, "Science Fiction");
        when(genreRepository.save(new Genre(0, "Science Fiction"))).thenReturn(savedGenre);

        var result = genreService.create("Science Fiction");

        assertThat(result).isEqualTo(savedGenre);
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getName()).isEqualTo("Science Fiction");
        verify(genreRepository).save(new Genre(0, "Science Fiction"));
    }
}
