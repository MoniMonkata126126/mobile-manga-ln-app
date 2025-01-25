package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.ChapterBeta;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterBetaRepository extends JpaRepository<ChapterBeta, Integer> {
    Optional<List<ChapterBeta>> findByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM ChapterBeta cb WHERE cb.id = :id AND cb.name = :name")
    void deleteChapterBetaByNameAndId(int id, String name);
}
