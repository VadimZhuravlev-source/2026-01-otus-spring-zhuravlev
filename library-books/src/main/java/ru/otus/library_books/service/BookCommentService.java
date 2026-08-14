package ru.otus.library_books.service;

import ru.otus.library_books.domain.BookComment;

import java.util.List;

public interface BookCommentService {

    List<BookComment> findByBookId(long bookId);
    public BookComment findById(long id);
    BookComment create(long bookId, String text);
    BookComment update(long id, String text);
    void deleteById(long id);

}
