package ru.otus.library_books.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.repository.BookCommentRepository;

@Service
@AllArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<BookComment> findByBookId(long bookId) {
        return bookCommentRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public BookComment findById(long id) {
        return bookCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id %d not found".formatted(id)));
    }

    public BookComment create(long bookId, String text) {
        return bookCommentRepository.insert(text, bookId);
    }

    @Transactional
    public BookComment update(long id, String text) {
        var comment = findById(id);
        comment.setText(text);
        entityManager.merge(comment);
        return comment;
    }

    @Transactional
    public void deleteById(long id) {
        var comment = findById(id);
        entityManager.remove(comment);
    }
}