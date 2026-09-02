package ru.otus.library_books.controller;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import ru.otus.library_books.config.JwtAuthenticationFilter;
import ru.otus.library_books.config.SecurityConfig;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.BookComment;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.service.AuthorService;
import ru.otus.library_books.service.BookCommentService;
import ru.otus.library_books.service.BookService;
import ru.otus.library_books.service.GenreService;

// Проверяем, что все ресурсы защищены JWT-аутентификацией
@WebMvcTest({BookController.class, AuthorController.class, GenreController.class, BookCommentController.class})
@Import(SecurityConfig.class)
class AllControllersSecurityTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
    void setUp() throws Exception {
        doAnswer(invocation -> {
            invocation.<jakarta.servlet.FilterChain>getArgument(2).doFilter(
                    invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void shouldAllowBookApiForUser() throws Exception {
        var author = new Author(1L, "Author");
        var genre = new Genre(1L, "Genre");
        var book = new Book(1L, "Book", author, genre, List.of());

        when(bookService.findAll()).thenReturn(List.of());
        when(bookService.findById(1L)).thenReturn(book);
        when(bookCommentService.findByBookId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/books")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/books/1/comments")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void shouldRejectOtherApisForUser() throws Exception {
        mockMvc.perform(get("/api/authors")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/genres")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/comments/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldAllowOtherApisForAdmin() throws Exception {
        var author = new Author(1L, "Author");
        var genre = new Genre(1L, "Genre");
        var book = new Book(1L, "Book", author, genre, List.of());

        when(authorService.findAll()).thenReturn(List.of());
        when(genreService.findAll()).thenReturn(List.of());
        when(bookCommentService.findById(1L)).thenReturn(new BookComment(1L, "text", book));

        mockMvc.perform(get("/api/authors")).andExpect(status().isOk());
        mockMvc.perform(get("/api/genres")).andExpect(status().isOk());
        mockMvc.perform(get("/api/comments/1")).andExpect(status().isOk());
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectObsoleteFormLoginPage() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isUnauthorized());
    }
}
