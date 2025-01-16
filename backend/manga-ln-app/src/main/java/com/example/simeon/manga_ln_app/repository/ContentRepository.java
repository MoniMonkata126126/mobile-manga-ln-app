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

    @Query(value =
            "WITH keywords AS ( " +
            "    SELECT unnest(string_to_array(:keywords, ',')) AS keyword " +
            "), match_threshold AS ( " +
            "    SELECT :matchCount AS match_count " +
            ") " +
            "SELECT c.*, COUNT(k.keyword) AS match_count " +
            "FROM content c " +
            "CROSS JOIN keywords k " +
            "CROSS JOIN match_threshold mt " +
            "WHERE c.name ILIKE '%' || k.keyword || '%' " +
            "GROUP BY c.id, c.name, mt.match_count " +
            "HAVING COUNT(k.keyword) >= mt.match_count " +
            "ORDER BY match_count DESC",
            nativeQuery = true)
    Optional<List<Content>> findByKeywords(@Param("keywords") String keywords,
                                 @Param("matchCount") int matchCount);
}
