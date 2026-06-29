package ru.otus.library_books.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.repository.BookCommentRepository;

@Service
@AllArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    @Transactional(readOnly = true)
    public List<BookComment> findByBookId(long bookId) {
        return bookCommentRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public BookComment findById(long id) {
        return bookCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id %d not found".formatted(id)));
    }

    @Transactional
    public BookComment create(long bookId, String text) {
        var book = bookService.findById(bookId);
        return bookCommentRepository.save(new BookComment(0, text, book));
    }

    @Transactional
    public BookComment update(long id, String text) {
        var comment = findById(id);
        comment.setText(text);
        return bookCommentRepository.save(comment);
    }

    @Transactional
    public void deleteById(long id) {
        findById(id);
        bookCommentRepository.deleteById(id);
    }
}