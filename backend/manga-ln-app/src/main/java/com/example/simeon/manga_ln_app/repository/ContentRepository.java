package com.example.simeon.manga_ln_app.repository;

import com.example.simeon.manga_ln_app.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Optional<Content> findByName(String name);
    
    @Query("SELECT c FROM Content c LEFT JOIN FETCH c.author WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Content> findByNameContainingKeywordIgnoreCase(@Param("keyword") String keyword);
}
