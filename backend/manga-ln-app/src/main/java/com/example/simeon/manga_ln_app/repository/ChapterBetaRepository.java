package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.ChapterBeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterBetaRepository extends JpaRepository<ChapterBeta, Integer> {
    Optional<ChapterBeta> findByName(String name);
}
