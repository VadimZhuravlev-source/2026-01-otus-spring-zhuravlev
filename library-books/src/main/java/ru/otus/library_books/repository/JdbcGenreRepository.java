package ru.otus.library_books.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import ru.otus.library_books.domain.Genre;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcGenreRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("""
                select id, name
                from genres
                order by id
                """, (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> findById(long id) {
        var genres = jdbcTemplate.query("""
                select id, name
                from genres
                where id = :id
                """, Map.of("id", id), (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name")));
        return genres.stream().findFirst();
    }

    @Override
    public Genre insert(String name) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("""
                insert into genres(name)
                values (:name)
                """, new MapSqlParameterSource()
                .addValue("name", name), keyHolder);
        return findById(keyHolder.getKeyAs(Long.class)).orElseThrow();
    }

}
