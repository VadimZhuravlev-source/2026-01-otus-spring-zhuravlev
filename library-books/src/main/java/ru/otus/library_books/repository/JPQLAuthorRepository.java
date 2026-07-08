package ru.otus.library_books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library_books.domain.Author;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JPQLAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        Query query = entityManager.createQuery("""
                select a
                from Author a
                order by a.id
                """, Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        Query query = entityManager.createQuery("""
                select a
                from Author a
                where a.id = :id
                """, Author.class);
        query.setParameter("id", id);
        Author author = (Author) query.getSingleResultOrNull();
        return Optional.ofNullable(author);
    }

    @Override
    @Transactional
    public Author insert(String fullName) {
        Author author = new Author(0, fullName);
        entityManager.persist(author);
        return author;
    }
}
