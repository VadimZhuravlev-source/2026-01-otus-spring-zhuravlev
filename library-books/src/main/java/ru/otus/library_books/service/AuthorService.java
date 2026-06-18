package ru.otus.library_books.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author findById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id %d not found".formatted(id)));
    }

    public Author create(String name) {
        return authorRepository.insert(name);
    }

}
