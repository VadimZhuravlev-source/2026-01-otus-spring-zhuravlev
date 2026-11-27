package ru.otus.library_books.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library_books.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

