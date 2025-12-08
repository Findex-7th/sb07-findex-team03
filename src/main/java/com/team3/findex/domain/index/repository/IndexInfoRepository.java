package com.team3.findex.domain.index.repository;

import com.team3.findex.domain.index.IndexInfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>, IndexInfoRepositoryCustom {
  boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification, String indexName);

  List<IndexInfo> findByIndexClassificationInAndIndexNameIn(List<String> indexClassifications, List<String> indexNames);

}