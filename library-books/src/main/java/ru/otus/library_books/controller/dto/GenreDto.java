package ru.otus.library_books.controller.dto;

import ru.otus.library_books.domain.Genre;

// DTO для отдачи жанра клиенту
public record GenreDto(long id, String name) {

    public static GenreDto from(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
