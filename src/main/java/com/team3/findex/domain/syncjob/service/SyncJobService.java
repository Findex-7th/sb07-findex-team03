package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.synclog.dto.SyncJobDto;

public class SyncJobService {
    public SyncJob create(SyncJobDto syncJobDto, IndexInfo indexInfo){
        SyncJob syncJob = new SyncJob(syncJobDto.jobType(),syncJobDto.worker(), indexInfo);
        return syncJob;
    }
}