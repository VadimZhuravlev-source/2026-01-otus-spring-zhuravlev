package ru.otus.library_books.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JPQLBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        Query query = entityManager.createQuery("""
                select b
                from Book b
                join fetch b.author
                join fetch b.genre
                order by b.id
                """, Book.class);
        //EntityGraph<?> graph = entityManager.getEntityGraph("Book.comments");
        //query.setHint("jakarta.persistence.fetchgraph", graph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        Query query = entityManager.createQuery("""
                select b
                from Book b
                join fetch b.author
                join fetch b.genre
                where b.id = :id
                """, Book.class);
        query.setParameter("id", id);
        EntityGraph<?> graph = entityManager.getEntityGraph("Book.comments");
        query.setHint("jakarta.persistence.fetchgraph", graph);
        Book book = (Book) query.getSingleResultOrNull();
        return Optional.ofNullable(book);
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, long genreId) {
        Query authorQuery = entityManager.createQuery("""
                select a
                from Author a
                where a.id = :id
                """, Author.class);
        authorQuery.setParameter("id", authorId);
        Author author = (Author) authorQuery.getSingleResultOrNull();

        Query genreQuery = entityManager.createQuery("""
                select g
                from Genre g
                where g.id = :id
                """, Genre.class);
        genreQuery.setParameter("id", genreId);
        Genre genre = (Genre) genreQuery.getSingleResultOrNull();

        Book book = new Book(0, title, author, genre, null);
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, long genreId) {
        Query bookQuery = entityManager.createQuery("""
                select b
                from Book b
                where b.id = :id
                """, Book.class);
        bookQuery.setParameter("id", id);
        Book book = (Book) bookQuery.getSingleResultOrNull();
        if (book == null) {
            throw new IllegalArgumentException("Book with id %d not found".formatted(id));
        }

        Query authorQuery = entityManager.createQuery("""
                select a
                from Author a
                where a.id = :id
                """, Author.class);
        authorQuery.setParameter("id", authorId);
        Author author = (Author) authorQuery.getSingleResultOrNull();

        Query genreQuery = entityManager.createQuery("""
                select g
                from Genre g
                where g.id = :id
                """, Genre.class);
        genreQuery.setParameter("id", genreId);
        Genre genre = (Genre) genreQuery.getSingleResultOrNull();

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        entityManager.merge(book);
        return book;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Query query = entityManager.createQuery("""
                select b
                from Book b
                where b.id = :id
                """, Book.class);
        query.setParameter("id", id);
        Book book = (Book) query.getSingleResultOrNull();
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
