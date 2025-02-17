package com.example.repository;

import com.example.entity.User;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(String userId);

    void deleteById(String userId);
}
