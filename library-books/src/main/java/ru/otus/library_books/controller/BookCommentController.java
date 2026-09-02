package ru.otus.library_books.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.otus.library_books.controller.dto.BookCommentDto;
import ru.otus.library_books.controller.dto.BookCommentRequest;
import ru.otus.library_books.service.BookCommentService;

@RestController
@AllArgsConstructor
public class BookCommentController {

    private final BookCommentService bookCommentService;

    @GetMapping("/api/books/{bookId}/comments")
    public List<BookCommentDto> findByBookId(@PathVariable long bookId) {
        return bookCommentService.findByBookId(bookId).stream().map(BookCommentDto::from).toList();
    }

    @GetMapping("/api/comments/{id}")
    public BookCommentDto findById(@PathVariable long id) {
        return BookCommentDto.from(bookCommentService.findById(id));
    }

    @PostMapping("/api/books/{bookId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public BookCommentDto create(@PathVariable long bookId, @RequestBody BookCommentRequest request) {
        return BookCommentDto.from(bookCommentService.create(bookId, request.getText()));
    }

    @PutMapping("/api/comments/{id}")
    public BookCommentDto update(@PathVariable long id, @RequestBody BookCommentRequest request) {
        return BookCommentDto.from(bookCommentService.update(id, request.getText()));
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        bookCommentService.deleteById(id);
    }
}
