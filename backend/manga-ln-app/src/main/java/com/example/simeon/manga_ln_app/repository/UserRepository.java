package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<Boolean> existsByUsername(String username);
}
