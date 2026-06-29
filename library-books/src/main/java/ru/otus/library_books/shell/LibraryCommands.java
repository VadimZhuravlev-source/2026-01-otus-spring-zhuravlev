package ru.otus.library_books.shell;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.service.AuthorService;
import ru.otus.library_books.service.BookService;
import ru.otus.library_books.service.GenreService;

@ShellComponent
public class LibraryCommands {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    public LibraryCommands(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @ShellMethod(value = "Books", key = {"books", "bs"})
    public String books() {
        return formatBooks(bookService.findAll());
    }

    @ShellMethod(value = "Book: b --id <id>", key = {"book", "b"})
    public String book(long id) {
        return formatBook(bookService.findById(id));
    }

    @ShellMethod(value = "Add book: ab --title <title> --author-id <authorId> --genre-id <genreId>", key = {"add-book", "ab"})
    public String addBook(@ShellOption(help = "Book title") String title,
                          @ShellOption(help = "Existing author id") long authorId,
                          @ShellOption(help = "Existing genre id") long genreId) {
        return formatBook(bookService.create(title, authorId, genreId));
    }

    @ShellMethod(value = "Edit book: eb --id <id> --title <title> --author-id <authorId> --genre-id <genreId>", key = {"edit-book", "eb"})
    public String editBook(@ShellOption(help = "Book id") long id,
                           @ShellOption(help = "New book title") String title,
                           @ShellOption(help = "Existing author id") long authorId,
                           @ShellOption(help = "Existing genre id") long genreId) {
        return formatBook(bookService.update(id, title, authorId, genreId));
    }

    @ShellMethod(value = "Delete book: db --id <id>", key = {"delete-book", "db"})
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

    @ShellMethod(value = "Add author: aauth --name <fullName>", key = {"add-author", "aauth"})
    public String addAuthor(@ShellOption(help = "Author full name") String name) {
        return formatAuthor(authorService.create(name));
    }

    @ShellMethod(value = "Genres", key = {"genres", "gs"})
    public String genres() {
        return genreService.findAll().stream()
                .map(this::formatGenre)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Add genre: ag --full-name <name>", key = {"add-genre", "ag"})
    public String addBook(@ShellOption(help = "Genre name") String fullName) {
        return formatGenre(genreService.create(fullName));
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
}
