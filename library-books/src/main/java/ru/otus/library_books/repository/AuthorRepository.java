package ru.otus.library_books.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.library_books.domain.Author;

public interface AuthorRepository {

    List<Author> findAll();

    Optional<Author> findById(long id);

    Author insert(String fullName);
}
