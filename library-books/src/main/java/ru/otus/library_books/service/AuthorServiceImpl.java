package ru.otus.library_books.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Author;
import ru.otus.library_books.repository.AuthorRepository;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author findById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id %d not found".formatted(id)));
    }

    public Author create(String name) {
        Author newGenre = new Author(0, name);
        return authorRepository.save(newGenre);
    }

}
