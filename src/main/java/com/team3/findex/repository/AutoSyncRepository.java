package com.team3.findex.repository;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.index.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoSyncRepository extends JpaRepository<AutoSync, Long>, AutoSyncRepositoryCustom {

  boolean existsByIndexInfo(IndexInfo IndexInfo);

}
