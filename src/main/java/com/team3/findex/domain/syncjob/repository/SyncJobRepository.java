package com.team3.findex.domain.syncjob.repository;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.enums.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SyncJobRepository extends JpaRepository<SyncJob, Long>, SyncJobRepositoryCustom{

    /**
     * 지수 가장 최근 성공 연동 기록조회
     * @param indexInfo 조회할 지수 정보
     * @param result 결과 상태
     * @return 최근 SyncJob (없으면 empty)
     */
    @Query("""
        SELECT sj
        FROM SyncJob sj
        WHERE sj.indexInfo = :indexinfo
        AND sj.result = :result
        ORDER BY sj.targetDate DESC
        limit 1
    """)
    Optional <SyncJob> findLatest(
            @Param("indexInfo") IndexInfo indexInfo,
            @Param("result") Result result
    );
}
