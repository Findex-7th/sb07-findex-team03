package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncJobService {

    private final SyncJobRepository syncJobRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobMapper syncJobMapper;


    @Transactional
    public List<SyncJobDto> createSyncJobs(JobType jobType, String worker){
        List<IndexInfo> indexInfos = indexInfoRepository.findAll();
        return indexInfos.stream()
                .map(indexInfo -> {
                    if(jobType==null){
                        log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexInfo.getId(), "작업 유형을 확인 할 수 없습니다.");
                        return createFailureLog(jobType, worker, indexInfo);
                    }
                    if(worker == null || worker.isBlank()){
                        log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexInfo.getId(), "작업자를 확인 할 수 없습니다.");
                        return createFailureLog(jobType, worker, indexInfo);
                    }
                    if(indexInfo == null) {
                        log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexInfo.getId(), "지수 정보를 확인 할 수 없습니다.");
                        return createFailureLog(jobType, worker, indexInfo);
                    }
                    return createSuccessLog(jobType, worker, indexInfo);
                })
                .map(syncJobMapper::toDto)
                .toList();
    }

    @Transactional
    protected SyncJob createSuccessLog(JobType jobType, String worker, IndexInfo indexInfo) {
        SyncJob successJob = SyncJob.ofSuccess(jobType, worker, indexInfo);
         return syncJobRepository.save(successJob);
    }

    @Transactional
    protected SyncJob createFailureLog(JobType jobType, String worker, IndexInfo indexInfo) {
        SyncJob failJob = SyncJob.ofFailure(jobType, worker, indexInfo);
        return syncJobRepository.save(failJob);
    }
}