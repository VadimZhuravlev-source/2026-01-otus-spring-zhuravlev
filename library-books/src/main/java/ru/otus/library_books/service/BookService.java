package ru.otus.library_books.service;

import ru.otus.library_books.domain.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    Book findById(long id);
    Book create(String title, long authorId, long genreId);
    Book update(long id, String title, long authorId, long genreId);
    void deleteById(long id);

}
