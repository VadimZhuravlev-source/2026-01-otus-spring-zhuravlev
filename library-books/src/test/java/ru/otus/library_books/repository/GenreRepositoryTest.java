package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.library_books.domain.Genre;

@DataJpaTest
@Import(JPQLGenreRepository.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("should find all genres")
    void shouldFindAllGenres() {
        var genres = genreRepository.findAll();

        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(Genre::getName)
                .containsExactlyInAnyOrder("Novel", "Adventure", "Dystopia");
    }

    @Test
    @DisplayName("should create genre")
    void shouldCreateGenre() {
        var genre = genreRepository.insert("Drama");

        assertThat(genre.getId()).isPositive();
        assertThat(genre.getName()).isEqualTo("Drama");
        assertThat(genreRepository.findById(genre.getId())).contains(genre);
    }

    @Test
    @DisplayName("should find genre by id")
    void shouldFindGenreById() {
        var genre = genreRepository.findById(2L);

        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isEqualTo("Adventure");
    }

    @Test
    @DisplayName("should return empty optional for missing genre")
    void shouldReturnEmptyOptionalForMissingGenre() {
        var genre = genreRepository.findById(100L);

        assertThat(genre).isEmpty();
    }
}