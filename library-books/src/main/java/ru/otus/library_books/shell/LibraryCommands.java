package ru.otus.library_books.shell;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.service.*;

@ShellComponent
@AllArgsConstructor
public class LibraryCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookCommentService bookCommentService;

    @ShellMethod(value = "Books", key = {"books", "bs"})
    public String books() {
        return formatBooks(bookService.findAll());
    }

    @ShellMethod(value = "Book: b <id>", key = {"book", "b"})
    public String book(long id) {
        return formatBook(bookService.findById(id));
    }

    @ShellMethod(value = "Add book: ab <title> <authorId> <genreId>", key = {"add-book", "ab"})
    public String addBook(@ShellOption(help = "Book title") String title,
                          @ShellOption(help = "Existing author id") long authorId,
                          @ShellOption(help = "Existing genre id") long genreId) {
        return formatBook(bookService.create(title, authorId, genreId));
    }

    @ShellMethod(value = "Edit book: eb <id> <title> <authorId> <genreId>", key = {"edit-book", "eb"})
    public String editBook(@ShellOption(help = "Book id") long id,
                           @ShellOption(help = "New book title") String title,
                           @ShellOption(help = "Existing author id") long authorId,
                           @ShellOption(help = "Existing genre id") long genreId) {
        return formatBook(bookService.update(id, title, authorId, genreId));
    }

    @ShellMethod(value = "Delete book: db <id>", key = {"delete-book", "db"})
    public String deleteBook(long id) {
        bookService.deleteById(id);
        return "Book with id %d deleted".formatted(id);
    }

    @ShellMethod(value = "Authors", key = {"authors", "auths"})
    public String authors() {
        return authorService.findAll().stream()
                .map(this::formatAuthor)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Add author: aauth <fullName>", key = {"add-author", "aauth"})
    public String addAuthor(@ShellOption(help = "Author full name") String name) {
        return formatAuthor(authorService.create(name));
    }

    @ShellMethod(value = "Genres", key = {"genres", "gs"})
    public String genres() {
        return genreService.findAll().stream()
                .map(this::formatGenre)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Add genre: ag <name>", key = {"add-genre", "ag"})
    public String addBook(@ShellOption(help = "Genre name") String fullName) {
        return formatGenre(genreService.create(fullName));
    }

    @ShellMethod(value = "Book comments: comments <bookId>", key = {"book-comments", "comments"})
    public String comments(long bookId) {
        return bookCommentService.findByBookId(bookId).stream()
                .map(this::formatComment)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Add comment: ac <bookId> <text>", key = {"add-comment", "ac"})
    public String addComment(@ShellOption(help = "Existing book id") long bookId,
                             @ShellOption(help = "Comment text") String text) {
        return formatComment(bookCommentService.create(bookId, text));
    }

    @ShellMethod(value = "Edit comment: ec <id> <text>", key = {"edit-comment", "ec"})
    public String editComment(@ShellOption(help = "Comment id") long id,
                              @ShellOption(help = "New comment text") String text) {
        return formatComment(bookCommentService.update(id, text));
    }

    @ShellMethod(value = "Delete comment: dc <id>", key = {"delete-comment", "dc"})
    public String deleteComment(long id) {
        bookCommentService.deleteById(id);
        return "Comment with id %d deleted".formatted(id);
    }

    private String formatBooks(List<Book> books) {
        return books.stream()
                .map(this::formatBook)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String formatBook(Book book) {
        return "%d. %s | %s | %s".formatted(
                book.getId(), book.getTitle(), book.getAuthor().getFullName(), book.getGenre().getName());
    }

    private String formatAuthor(Author author) {
        return "%d. %s".formatted(author.getId(), author.getFullName());
    }

    private String formatGenre(Genre genre) {
        return "%d. %s".formatted(genre.getId(), genre.getName());
    }

    private String formatComment(BookComment comment) {
        return "%d. %s | bookId=%d".formatted(comment.getId(), comment.getText(), comment.getBook().getId());
    }

}
