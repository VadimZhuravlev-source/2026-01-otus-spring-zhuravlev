package ru.otus.library_books.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.library_books.domain.Genre;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Genre insert(String name);
}
