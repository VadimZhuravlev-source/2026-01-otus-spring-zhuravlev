package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.library_books.domain.Author;

@DataJpaTest
@Import(JPQLAuthorRepository.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("should find all authors")
    void shouldFindAllAuthors() {
        var authors = authorRepository.findAll();

        assertThat(authors).hasSize(3);
        assertThat(authors).extracting(Author::getFullName)
                .containsExactlyInAnyOrder("Fyodor Dostoevsky", "Jules Verne", "George Orwell");
    }

    @Test
    @DisplayName("should create author")
    void shouldCreateAuthor() {
        var author = authorRepository.insert("Leo Tolstoy");

        assertThat(author.getId()).isPositive();
        assertThat(author.getFullName()).isEqualTo("Leo Tolstoy");
        assertThat(authorRepository.findById(author.getId())).contains(author);
    }

    @Test
    @DisplayName("should find author by id")
    void shouldFindAuthorById() {
        var author = authorRepository.findById(1L);

        assertThat(author).isPresent();
        assertThat(author.get().getFullName()).isEqualTo("Fyodor Dostoevsky");
    }

    @Test
    @DisplayName("should return empty optional for missing author")
    void shouldReturnEmptyOptionalForMissingAuthor() {
        var author = authorRepository.findById(100L);

        assertThat(author).isEmpty();
    }
}