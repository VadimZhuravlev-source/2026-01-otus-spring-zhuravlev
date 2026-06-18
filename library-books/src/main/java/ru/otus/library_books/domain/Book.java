package ru.otus.library_books.domain;

public record Book(long id, String title, Author author, Genre genre) {
}
