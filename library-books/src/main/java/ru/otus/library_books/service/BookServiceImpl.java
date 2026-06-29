package ru.otus.library_books.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.BookRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %d not found".formatted(id)));
    }

    public Book create(String title, long authorId, long genreId) {
        Author foundAuthor = authorService.findById(authorId);
        Genre foundGenre = genreService.findById(genreId);
        Book newBook = new Book(0, title, foundAuthor, foundGenre);
        return bookRepository.save(newBook);
    }

    public Book update(long id, String title, long authorId, long genreId) {
        Book foundBook = findById(id);
        Author foundAuthor = authorService.findById(authorId);
        Genre foundGenre = genreService.findById(genreId);
        foundBook.setTitle(title);
        foundBook.setAuthor(foundAuthor);
        foundBook.setGenre(foundGenre);
        return bookRepository.save(foundBook);
    }

    public void deleteById(long id) {
        findById(id);
        bookRepository.deleteById(id);
    }
}
