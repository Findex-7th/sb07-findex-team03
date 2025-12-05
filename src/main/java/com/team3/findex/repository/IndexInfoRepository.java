package com.team3.findex.repository;

import com.team3.findex.domain.index.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification, String indexName);

  @Query("""
    SELECT i FROM IndexInfo i
    WHERE (:classification IS NULL OR i.indexClassification LIKE %:classification%)
      AND (:name IS NULL OR i.indexName LIKE %:name%)
      AND (:favorite IS NULL OR i.favorite = :favorite)
      AND (:cursorId IS NULL OR i.id > :cursorId)
    """)
  List<IndexInfo> searchWithCursor(
      @Param("classification") String classification,
      @Param("name") String name,
      @Param("favorite") Boolean favorite,
      @Param("cursorId") Long cursorId
  );

}