package ru.otus.library_books.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.library_books.controller.dto.BookRequest;
import ru.otus.library_books.domain.Author;
import ru.otus.library_books.domain.Book;
import ru.otus.library_books.domain.Genre;
import ru.otus.library_books.service.BookService;
import ru.otus.library_books.config.JwtAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

// Тесты Spring MVC слоя контроллера книг с мокированием сервисного слоя
@WebMvcTest(
        controllers = BookController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "user")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    @DisplayName("should return list of books")
    void shouldReturnAllBooks() throws Exception {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var book1 = new Book(1L, "Crime and Punishment", author, genre, null);
        var book2 = new Book(2L, "The Idiot", author, genre, null);
        when(bookService.findAll()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Crime and Punishment"))
                .andExpect(jsonPath("$[1].title").value("The Idiot"));

        verify(bookService).findAll();
    }

    @Test
    @DisplayName("should return book by id")
    void shouldReturnBookById() throws Exception {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var book = new Book(1L, "Crime and Punishment", author, genre, null);
        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Crime and Punishment"))
                .andExpect(jsonPath("$.author.fullName").value("Fyodor Dostoevsky"))
                .andExpect(jsonPath("$.genre.name").value("Psychological Fiction"));

        verify(bookService).findById(1L);
    }

    @Test
    @DisplayName("should return 404 when book not found by id")
    void shouldReturnErrorWhenBookNotFound() throws Exception {
        when(bookService.findById(100L))
                .thenThrow(new IllegalArgumentException("Book with id 100 not found"));

        mockMvc.perform(get("/api/books/{id}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id 100 not found"));

        verify(bookService).findById(100L);
    }

    @Test
    @DisplayName("should create book")
    void shouldCreateBook() throws Exception {
        var author = new Author(1L, "Fyodor Dostoevsky");
        var genre = new Genre(1L, "Psychological Fiction");
        var savedBook = new Book(1L, "The Gambler", author, genre, null);
        var request = new BookRequest("The Gambler", 1L, 1L);
        when(bookService.create(eq("The Gambler"), eq(1L), eq(1L))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Gambler"));

        verify(bookService).create("The Gambler", 1L, 1L);
    }

    @Test
    @DisplayName("should update book")
    void shouldUpdateBook() throws Exception {
        var author = new Author(2L, "Jules Verne");
        var genre = new Genre(2L, "Adventure");
        var updatedBook = new Book(1L, "New Title", author, genre, null);
        var request = new BookRequest("New Title", 2L, 2L);
        when(bookService.update(eq(1L), eq("New Title"), eq(2L), eq(2L))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author.fullName").value("Jules Verne"));

        verify(bookService).update(1L, "New Title", 2L, 2L);
    }

    @Test
    @DisplayName("should delete book by id")
    void shouldDeleteBookById() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(1L);
    }
}
