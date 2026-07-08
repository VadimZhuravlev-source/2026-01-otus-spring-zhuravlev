package ru.otus.library_books.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.library_books.domain.BookComment;

public interface BookCommentRepository {

    List<BookComment> findByBookId(long bookId);
    List<BookComment> findAll();
    Optional<BookComment> findById(long id);
    BookComment insert(String text, long bookId);
    BookComment update(long commentId, String newText, long bookId);
}