package ru.otus.library_books.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.library_books.config.SecurityConfig;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.service.AuthorService;
import ru.otus.library_books.service.BookCommentService;
import ru.otus.library_books.service.BookService;
import ru.otus.library_books.service.GenreService;

// Проверяем, что все ресурсы защищены, а страница логина доступна всем
@WebMvcTest({BookController.class, AuthorController.class, GenreController.class, BookCommentController.class})
@Import(SecurityConfig.class)
class AllControllersSecurityTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private BookCommentService bookCommentService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user")
    void shouldAllowAllApiEndpointsForAuthenticatedUser() throws Exception {
        var author = new Author(1L, "Author");
        var genre = new Genre(1L, "Genre");
        var book = new Book(1L, "Book", author, genre, List.of());

        when(bookService.findAll()).thenReturn(List.of());
        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());
        when(bookCommentService.findByBookId(1L)).thenReturn(List.of());

        when(bookService.findById(1L)).thenReturn(book);
        when(authorService.findById(1L)).thenReturn(author);
        when(genreService.findById(1L)).thenReturn(genre);
        when(bookCommentService.findById(1L)).thenReturn(new BookComment(1L, "text", book));

        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/authors")).andExpect(status().isOk());
        mockMvc.perform(get("/api/authors/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/genres")).andExpect(status().isOk());
        mockMvc.perform(get("/api/genres/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1/comments")).andExpect(status().isOk());
        mockMvc.perform(get("/api/comments/1")).andExpect(status().isOk());
    }

    @Test
    void shouldRedirectUnauthenticatedRequestsToLogin() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("/login")));
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("/login")));
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", containsString("/login")));
    }

    @Test
    void shouldAllowLoginPageForAnonymous() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }
}
