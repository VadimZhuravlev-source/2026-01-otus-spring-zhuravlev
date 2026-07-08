package ru.otus.library_books.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Book;
import ru.otus.library_books.repository.BookRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %d not found".formatted(id)));
    }

    public Book create(String title, long authorId, long genreId) {
        return bookRepository.insert(title, authorId, genreId);
    }

    public Book update(long id, String title, long authorId, long genreId) {
        return bookRepository.update(id, title, authorId, genreId);
    }

    public void deleteById(long id) {
        findById(id);
        bookRepository.deleteById(id);
    }
}
