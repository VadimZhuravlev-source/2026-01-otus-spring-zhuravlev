package ru.otus.library_books.service;

import ru.otus.library_books.domain.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> findAll();
    Genre findById(long id);
    Genre create(String fullName);

}
