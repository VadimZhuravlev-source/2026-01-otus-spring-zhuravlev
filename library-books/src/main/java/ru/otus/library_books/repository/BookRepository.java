package ru.otus.library_books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.Book;

public interface BookRepository  extends JpaRepository<Book, Long> {

}
