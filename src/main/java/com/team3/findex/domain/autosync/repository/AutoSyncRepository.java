package com.team3.findex.domain.autosync.repository;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.index.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutoSyncRepository extends JpaRepository<AutoSync, Long>, AutoSyncRepositoryCustom {

    boolean existsByIndexInfo(IndexInfo IndexInfo);

    void deleteByIndexInfoId(Long indexInfoId);

    List<AutoSync> findByIndexInfoIn(List<IndexInfo> allProcessedInfos);

    @Query("SELECT a FROM AutoSync a JOIN FETCH a.indexInfo WHERE a.isEnable = true")
    List<AutoSync> findEnabledIndexInfos();
}
