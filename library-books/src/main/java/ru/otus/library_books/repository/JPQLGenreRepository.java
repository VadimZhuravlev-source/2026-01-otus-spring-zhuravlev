package ru.otus.library_books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library_books.domain.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JPQLGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        Query query = entityManager.createQuery("""
                select g
                from Genre g
                order by g.id
                """, Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        Query query = entityManager.createQuery("""
                select g
                from Genre g
                where g.id = :id
                """, Genre.class);
        query.setParameter("id", id);
        Genre genre = (Genre) query.getSingleResultOrNull();
        return Optional.ofNullable(genre);
    }

    @Override
    @Transactional
    public Genre insert(String name) {
        Genre genre = new Genre(0, name);
        entityManager.persist(genre);
        return genre;
    }
}
