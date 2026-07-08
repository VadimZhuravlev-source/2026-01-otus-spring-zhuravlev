package ru.otus.library_books.repository;

import ru.otus.library_books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(long id);
    Book insert(String title, long authorId, long genreId);
    Book update(long id, String title, long authorId, long genreId);
    void deleteById(long id);
}
