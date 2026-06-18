package ru.otus.library_books.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Genre;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcAuthorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query("""
                select id, full_name
                from authors
                order by id
                """, (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("full_name")));
    }

    @Override
    public Optional<Author> findById(long id) {
        var authors = jdbcTemplate.query("""
                select id, full_name
                from authors
                where id = :id
                """, Map.of("id", id), (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("full_name")));
        return authors.stream().findFirst();
    }

    @Override
    public Author insert(String fullName) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("""
                insert into authors(fullName)
                values (:fullName)
                """, new MapSqlParameterSource()
                .addValue("fullName", fullName), keyHolder);
        return findById(keyHolder.getKeyAs(Long.class)).orElseThrow();
    }

}
