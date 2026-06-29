package ru.otus.library_books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.Genre;

public interface GenreRepository  extends JpaRepository<Genre, Long> {

}
