package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    Optional<List<Chapter>> findByName(String name);
}
