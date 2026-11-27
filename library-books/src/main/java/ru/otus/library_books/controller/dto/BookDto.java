package ru.otus.library_books.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.library_books.domain.Book;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private long id;
    private String title;
    private AuthorDto author;
    private GenreDto genre;
    private List<BookCommentDto> comments;

    public static BookDto from(Book book) {
        var comments = book.getBookComments() == null
                ? java.util.List.<BookCommentDto>of()
                : book.getBookComments().stream().map(BookCommentDto::from).toList();
        return new BookDto(book.getId(), book.getTitle(),
                ru.otus.library_books.controller.dto.AuthorDto.from(book.getAuthor()), ru.otus.library_books.controller.dto.GenreDto.from(book.getGenre()), comments);
    }

}
