package ru.otus.library_books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

class JwtServiceTest {

    private static final String SECRET = "bGlicmFyeS1ib29rcy10ZXN0LWp3dC1zZWNyZXQta2V5";

    @Test
    void shouldGenerateAndValidateToken() {
        var service = new JwtService(SECRET, 3_600_000);
        var user = new User("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        var token = service.generateToken(user);

        assertThat(service.extractUsername(token)).isEqualTo("user");
        assertThat(service.isTokenValid(token, user)).isTrue();
        assertThat(service.getExpiration()).isEqualTo(3_600_000);
    }

    @Test
    void shouldRejectTokenWithInvalidSignature() {
        var service = new JwtService(SECRET, 3_600_000);
        var otherService = new JwtService("YW5vdGhlci1saWJyYXJ5LWJvb2tzLWp3dC1zZWNyZXQta2V5", 3_600_000);
        var user = new User("user", "password", List.of());
        var token = service.generateToken(user);

        assertThatThrownBy(() -> otherService.extractUsername(token))
                .isInstanceOf(JwtException.class);
    }
}
