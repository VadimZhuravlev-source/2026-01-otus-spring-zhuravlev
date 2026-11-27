package ru.otus.library_books.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.library_books.domain.Author;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private long id;

    private String fullName;

    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

}