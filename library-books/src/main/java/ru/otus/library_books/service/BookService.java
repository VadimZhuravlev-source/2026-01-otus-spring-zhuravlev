package ru.otus.library_books.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Book;
import ru.otus.library_books.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    public BookService(BookRepository bookRepository, AuthorService authorService, GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %d not found".formatted(id)));
    }

    public Book create(String title, long authorId, long genreId) {
        authorService.findById(authorId);
        genreService.findById(genreId);
        return bookRepository.insert(title, authorId, genreId);
    }

    public Book update(long id, String title, long authorId, long genreId) {
        findById(id);
        authorService.findById(authorId);
        genreService.findById(genreId);
        return bookRepository.update(id, title, authorId, genreId);
    }

    public void deleteById(long id) {
        findById(id);
        bookRepository.deleteById(id);
    }
}
