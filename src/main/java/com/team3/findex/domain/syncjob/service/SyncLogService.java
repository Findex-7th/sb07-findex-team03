package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncLogService {

    private final SyncJobRepository syncJobRepository;

    /**
     * 성공 로그 저장 (별도 트랜잭션으로 분리 가능)
     * REQUIRES_NEW: 상위 트랜잭션과 무관하게 항상 새로운 트랜잭션으로 저장 (선택 사항, 필요시 적용)
     */
    @Transactional
    public void saveAllSuccessLogs(List<SyncJob> logs) {
        if (logs != null && !logs.isEmpty()) {
            syncJobRepository.saveAll(logs);
        }
    }

    /**
     * 실패 로그 저장
     * 상위 로직이 실패해서 롤백되더라도 로그는 남겨야 하므로 REQUIRES_NEW 권장
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SyncJob createFailureLog(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo) {
        SyncJob failJob = SyncJob.ofFailure(jobType, worker, targetDate, indexInfo);
        return syncJobRepository.save(failJob);
    }
}
