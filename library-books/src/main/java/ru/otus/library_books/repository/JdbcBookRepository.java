package ru.otus.library_books.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;

@Repository
public class JdbcBookRepository implements BookRepository {

    private static final String BOOK_SELECT = """
            select b.id as book_id, b.title, a.id as author_id, a.full_name, g.id as genre_id, g.name
            from books b
            join authors a on a.id = b.author_id
            join genres g on g.id = b.genre_id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcBookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(BOOK_SELECT + " order by b.id", new BookRowMapper());
    }

    @Override
    public Optional<Book> findById(long id) {
        var books = jdbcTemplate.query(BOOK_SELECT + " where b.id = :id", Map.of("id", id), new BookRowMapper());
        return books.stream().findFirst();
    }

    @Override
    public Book insert(String title, long authorId, long genreId) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("""
                insert into books(title, author_id, genre_id)
                values (:title, :authorId, :genreId)
                """, new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("authorId", authorId)
                .addValue("genreId", genreId), keyHolder);
        return findById(keyHolder.getKeyAs(Long.class)).orElseThrow();
    }

    @Override
    public Book update(long id, String title, long authorId, long genreId) {
        var updatedRows = jdbcTemplate.update("""
                update books
                set title = :title,
                    author_id = :authorId,
                    genre_id = :genreId
                where id = :id
                """, Map.of("id", id, "title", title, "authorId", authorId, "genreId", genreId));
        if (updatedRows == 0) {
            throw new IllegalArgumentException("Book with id %d not found".formatted(id));
        }
        return findById(id).orElseThrow();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from books where id = :id", Map.of("id", id));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
            var genre = new Genre(rs.getLong("genre_id"), rs.getString("name"));
            return new Book(rs.getLong("book_id"), rs.getString("title"), author, genre);
        }
    }
}
