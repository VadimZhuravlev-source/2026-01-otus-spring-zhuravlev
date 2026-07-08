package ru.otus.library_books.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.library_books.domain.Book;

@DataJpaTest
@Import({JPQLBookRepository.class, JPQLAuthorRepository.class, JPQLGenreRepository.class})
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("should find all books")
    void shouldFindAllBooks() {
        var books = bookRepository.findAll();

        assertThat(books).hasSize(3);
        assertThat(books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder(
                        "Crime and Punishment",
                        "Twenty Thousand Leagues Under the Seas",
                        "Nineteen Eighty-Four"
                );
    }

    @Test
    @DisplayName("should create update and delete book")
    void shouldCreateUpdateAndDeleteBook() {
        var book = bookRepository.insert("The Idiot", 1L, 1L);

        assertThat(book.getId()).isPositive();
        assertThat(book.getTitle()).isEqualTo("The Idiot");

        var updatedBook = bookRepository.update(book.getId(), "Updated title", 1L, 1L);

        assertThat(updatedBook.getTitle()).isEqualTo("Updated title");

        bookRepository.deleteById(updatedBook.getId());

        assertThat(bookRepository.findById(updatedBook.getId())).isEmpty();
    }

    @Test
    @DisplayName("should find seeded book by id")
    void shouldFindSeededBookById() {
        var book = bookRepository.findById(2L);

        assertThat(book).isPresent();
        assertThat(book.get().getTitle()).isEqualTo("Twenty Thousand Leagues Under the Seas");
    }

    @Test
    @DisplayName("should return empty optional for missing book")
    void shouldReturnEmptyOptionalForMissingBook() {
        var book = bookRepository.findById(100L);

        assertThat(book).isEmpty();
    }
}