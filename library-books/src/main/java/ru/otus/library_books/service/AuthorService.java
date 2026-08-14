package ru.otus.library_books.service;

import ru.otus.library_books.domain.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();
    Author findById(long id);
    Author create(String name);

}
