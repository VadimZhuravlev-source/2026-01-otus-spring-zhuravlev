package ru.otus.library_books.repository;

import ru.otus.library_books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();
    Optional<Genre> findById(long id);
    Genre insert(String name);
}
