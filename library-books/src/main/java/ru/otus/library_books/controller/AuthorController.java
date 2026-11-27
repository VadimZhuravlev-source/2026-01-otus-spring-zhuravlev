package ru.otus.library_books.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.library_books.controller.dto.AuthorDto;
import ru.otus.library_books.controller.dto.AuthorRequest;
import ru.otus.library_books.service.AuthorService;

import java.util.List;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<AuthorDto> findAll() {
        return authorService.findAll().stream().map(AuthorDto::from).toList();
    }

    @GetMapping("/api/authors/{id}")
    public AuthorDto findById(@PathVariable long id) {
        return AuthorDto.from(authorService.findById(id));
    }

    @PostMapping("/api/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody AuthorRequest request) {
        return AuthorDto.from(authorService.create(request.getFullName()));
    }
}
