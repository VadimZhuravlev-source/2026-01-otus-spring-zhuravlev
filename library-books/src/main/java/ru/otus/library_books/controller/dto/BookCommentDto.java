package ru.otus.library_books.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.library_books.domain.BookComment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentDto {

    private long id;
    private String text;
    private long bookId;

    public static BookCommentDto from(BookComment comment) {
        return new BookCommentDto(comment.getId(), comment.getText(), comment.getBook().getId());
    }

}
