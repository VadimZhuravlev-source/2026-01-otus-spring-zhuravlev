package ru.otus.library_books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
