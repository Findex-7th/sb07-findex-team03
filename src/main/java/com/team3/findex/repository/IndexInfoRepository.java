package com.team3.findex.repository;

import com.team3.findex.domain.index.IndexInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification, String indexName);
}