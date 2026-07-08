package ru.otus.library_books.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @EntityGraph(attributePaths = {"book"})
    List<BookComment> findByBookId(long bookId);
}
