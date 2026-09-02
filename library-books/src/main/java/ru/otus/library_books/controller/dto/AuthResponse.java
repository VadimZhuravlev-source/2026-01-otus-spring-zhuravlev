package ru.otus.library_books.controller.dto;

public record AuthResponse(String token, String type, long expiresIn) {
}
