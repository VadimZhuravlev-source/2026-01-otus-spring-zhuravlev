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
        assertThat(firstBook.title()).isEqualTo("Crime and Punishment");
        assertThat(firstBook.author().fullName()).isEqualTo("Fyodor Dostoevsky");
        assertThat(firstBook.genre().name()).isEqualTo("Novel");


    }

    @Test
    @DisplayName("should create update and delete book")
    void shouldCreateUpdateAndDeleteBook() {
        var created = bookRepository.insert("The Idiot", 1, 1);
        assertThat(created.id()).isPositive();
        assertThat(created.title()).isEqualTo("The Idiot");

        var updated = bookRepository.update(created.id(), "Journey to the Center of the Earth", 2, 2);

        assertThat(updated.title()).isEqualTo("Journey to the Center of the Earth");
        assertThat(updated.author().fullName()).isEqualTo("Jules Verne");
        assertThat(updated.genre().name()).isEqualTo("Adventure");

        bookRepository.deleteById(created.id());

        assertThat(bookRepository.findById(created.id())).isEmpty();
    }
}
