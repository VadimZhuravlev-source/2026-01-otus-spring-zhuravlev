package ru.otus.library_books.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.repository.GenreRepository;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findById(long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id %d not found".formatted(id)));
    }

    public Genre create(String fullName) {
        return genreRepository.insert(fullName);
    }

}
