package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.ContentBeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentBetaRepository extends JpaRepository<ContentBeta, Integer> {
    Optional<ContentBeta> findByName(String name);
}
