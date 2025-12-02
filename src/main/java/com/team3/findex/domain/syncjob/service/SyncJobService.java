package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.JobType;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncJobService {

    private final SyncJobRepository syncJobRepository;

    public SyncJob create(JobType jobType, String worker, IndexInfo indexInfo){
        if(jobType == null){
            throw new IllegalArgumentException("데이터 유형을 확인 할 수 없습니다.");
        }
        if(worker == null){
            throw new IllegalArgumentException("작업자를 확인 할 수 없습니다.");
        }
        if(indexInfo == null){
            throw new IllegalArgumentException("지수 정보를 확인 할 수 없습니다.");
        }
        return syncJobRepository.save(new SyncJob(jobType, worker, indexInfo));
    }


}