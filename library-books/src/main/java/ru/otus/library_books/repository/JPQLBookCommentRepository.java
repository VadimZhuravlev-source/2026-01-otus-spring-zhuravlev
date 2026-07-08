package ru.otus.library_books.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JPQLBookCommentRepository implements BookCommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<BookComment> findAll() {
        Query query = entityManager.createQuery("""
                select bc
                from BookComment bc
                order by id
                """, BookComment.class);
        EntityGraph graph = entityManager.getEntityGraph("BookComment.book");
        query.setHint("jakarta.persistence.fetchgraph", graph);
        return query.getResultList();
    }

    @Override
    public Optional<BookComment> findById(long id) {
        Query query = entityManager.createQuery("""
                select bc
                from BookComment bc
                where id = :id
                """, BookComment.class);
        query.setParameter("id", id);
        BookComment bookComment = (BookComment) query.getSingleResultOrNull();
        return Optional.ofNullable(bookComment);
    }

    @Override
    @Transactional
    public BookComment insert(String text, long bookId) {
        Query query = entityManager.createQuery("""
                select b
                from Book b
                where id = :id
                """, Book.class);
        query.setParameter("id", bookId);
        Book book = (Book) query.getSingleResultOrNull();
        BookComment bookComment = new BookComment(0, text,book);
        entityManager.persist(bookComment);
        return bookComment;
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        Query query = entityManager.createQuery("""
                select bc
                from BookComment bc
                where bc.book.id = :bookId
                """, BookComment.class);
        query.setParameter("bookId", bookId);
        EntityGraph graph = entityManager.getEntityGraph("BookComment.book");
        query.setHint("jakarta.persistence.fetchgraph", graph);
        return query.getResultList();
    }

    @Override
    public BookComment update(long commentId, String newText, long bookId) {

        Query query = entityManager.createQuery("""
                select b
                from Book b
                where id = :id
                """, Book.class);
        query.setParameter("id", bookId);
        Book book = (Book) query.getSingleResultOrNull();

        query = entityManager.createQuery("""
            select bc
            from BookComment bc
            where bc.id = :id
            """, BookComment.class);
        query.setParameter("id", commentId);
        BookComment bookComment = (BookComment) query.getSingleResultOrNull();
        if (bookComment == null) {
            throw new EntityNotFoundException("BookComment with id " + commentId + " not found");
        }
        bookComment.setText(newText);
        bookComment.setBook(book);
        entityManager.merge(bookComment);
        return bookComment;
    }

}
