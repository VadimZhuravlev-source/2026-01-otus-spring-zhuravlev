package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;

@JdbcTest
@Import({JdbcBookRepository.class, JdbcAuthorRepository.class, JdbcGenreRepository.class})
class JdbcBookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("should return all books with authors and genres")
    void shouldReturnAllBooksWithAuthorsAndGenres() {
        var books = bookRepository.findAll();

        assertThat(books).hasSize(3);
        var book = new Book(1L,"dfdf", new Author(2, "fdfd"),new Genre(1, "222"));
        var firstBook = books.stream().findFirst().orElse(book);
        assertThat(firstBook.getTitle()).isEqualTo("Crime and Punishment");
        assertThat(firstBook.getAuthor().getFullName()).isEqualTo("Fyodor Dostoevsky");
        assertThat(firstBook.getGenre().getName()).isEqualTo("Novel");


    }

    @Test
    @DisplayName("should create update and delete book")
    void shouldCreateUpdateAndDeleteBook() {
        var created = bookRepository.insert("The Idiot", 1, 1);
        assertThat(created.getId()).isPositive();
        assertThat(created.getTitle()).isEqualTo("The Idiot");

        var updated = bookRepository.update(created.getId(), "Journey to the Center of the Earth", 2, 2);

        assertThat(updated.getTitle()).isEqualTo("Journey to the Center of the Earth");
        assertThat(updated.getAuthor().getFullName()).isEqualTo("Jules Verne");
        assertThat(updated.getGenre().getName()).isEqualTo("Adventure");

        bookRepository.deleteById(created.getId());

        assertThat(bookRepository.findById(created.getId())).isEmpty();
    }
}
