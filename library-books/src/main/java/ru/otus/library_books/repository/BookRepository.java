package ru.otus.library_books.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author", "genre", "bookComments"})
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = {"author", "genre", "bookComments"})
    Optional<Book> findById(Long id);
}
