package ru.otus.library_books.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Book;
import ru.otus.library_books.repository.AuthorRepository;
import ru.otus.library_books.repository.BookRepository;
import ru.otus.library_books.repository.GenreRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %d not found".formatted(id)));
    }

    public Book create(String title, long authorId, long genreId) {
        var author = authorRepository.getReferenceById(authorId);
        var genre = genreRepository.getReferenceById(genreId);
        return bookRepository.save(new Book(0, title, author, genre, null));
    }

    public Book update(long id, String title, long authorId, long genreId) {
        var book = findById(id);
        var author = authorRepository.getReferenceById(authorId);
        var genre = genreRepository.getReferenceById(genreId);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    public void deleteById(long id) {
        findById(id);
        bookRepository.deleteById(id);
    }
}
