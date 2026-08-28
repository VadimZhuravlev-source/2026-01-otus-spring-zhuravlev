package ru.otus.library_books.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.otus.library_books.controller.dto.GenreDto;
import ru.otus.library_books.controller.dto.GenreRequest;
import ru.otus.library_books.service.GenreService;

@RestController
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<GenreDto> findAll() {
        return genreService.findAll().stream().map(GenreDto::from).toList();
    }

    @GetMapping("/api/genres/{id}")
    public GenreDto findById(@PathVariable long id) {
        return GenreDto.from(genreService.findById(id));
    }

    @PostMapping("/api/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@RequestBody GenreRequest request) {
        return GenreDto.from(genreService.create(request.getFullName()));
    }
}
