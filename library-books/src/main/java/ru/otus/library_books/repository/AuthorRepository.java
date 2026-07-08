package ru.otus.library_books.repository;

import ru.otus.library_books.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();
    Optional<Author> findById(long id);
    Author insert(String fullName);
}
