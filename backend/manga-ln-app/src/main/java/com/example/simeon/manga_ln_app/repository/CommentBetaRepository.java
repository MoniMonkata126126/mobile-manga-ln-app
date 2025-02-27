package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.CommentBeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentBetaRepository extends JpaRepository<CommentBeta, Integer> {
}
