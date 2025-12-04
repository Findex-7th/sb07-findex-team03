package com.team3.findex.domain.syncjob.repository;

import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import java.util.List;

public interface SyncJobRepositoryCustom {
    List<SyncJob> findAllByCursor(CursorPageRequestSyncJobDto request);
}