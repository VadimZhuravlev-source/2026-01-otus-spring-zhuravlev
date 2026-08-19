package ru.otus.library_books.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.otus.library_books.controller.dto.BookDto;
import ru.otus.library_books.controller.dto.BookRequest;
import ru.otus.library_books.service.BookService;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> findAll() {
        return bookService.findAll().stream().map(BookDto::from).toList();
    }

    @GetMapping("/api/books/{id}")
    public BookDto findById(@PathVariable long id) {
        return BookDto.from(bookService.findById(id));
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody BookRequest request) {
        return BookDto.from(bookService.create(request.getTitle(), request.getAuthorId(), request.getGenreId()));
    }

    @PutMapping("/api/books/{id}")
    public BookDto update(@PathVariable long id, @RequestBody BookRequest request) {
        return BookDto.from(bookService.update(id, request.getTitle(), request.getAuthorId(), request.getGenreId()));
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
