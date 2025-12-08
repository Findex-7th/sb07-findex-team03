package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import com.team3.findex.domain.syncjob.infrastructure.openapitester.OpenApiService;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncJobService {

    private final SyncJobRepository syncJobRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobMapper syncJobMapper;
    private final OpenApiService openApiService;
    private final SyncIndexService syncIndexService;
    private final SyncLogService syncLogService;

    public List<SyncJobDto> syncIndexInfos(String worker) {
        List<SyncJobDto> resultLogs = Collections.emptyList();
        List<IndexInfo> fetchedIndexInfos = Collections.emptyList();
        try {
             fetchedIndexInfos = openApiService.fetchAllApiToIndexInfo();
        }catch (Exception e){
            log.error("지수 정보 일괄 동기화 실패", e);
            SyncJob failLog = syncLogService.createFailureLog(
                    JobType.INDEX_INFO, worker, LocalDate.now(), null);
            resultLogs.add(syncJobMapper.toDto(failLog));
        }

        if (fetchedIndexInfos.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return syncIndexService.bulkSaveIndexInfosAndLog(fetchedIndexInfos, worker);

        } catch (Exception e) {
            log.error("지수 정보 일괄 동기화 실패", e);

            return Collections.emptyList();
        }
    }


    public List<SyncJobDto> syncIndexData(
            IndexDataSyncRequest indexDataSyncRequest,
            String worker
    ) {
        List<IndexInfo> indexInfos = indexInfoRepository.findAllById(indexDataSyncRequest.indexInfoIds());
        if (indexInfos.isEmpty()) throw new IllegalArgumentException("지수 정보가 존재하지 않습니다.");

        List<SyncJobDto> resultLogs = new ArrayList<>();

        for (IndexInfo indexInfo : indexInfos) {
            try {
                List<IndexData> fetchedDataList = openApiService.fetchApiByParamsToIndexData(
                        indexInfo.getIndexName(),
                        indexDataSyncRequest.baseDateFrom().replace("-", ""),
                        indexDataSyncRequest.baseDateTo().replace("-", ""),
                        indexInfo
                );

                if (fetchedDataList.isEmpty()) continue;

                List<SyncJobDto> logs = syncIndexService.bulkSaveIndexDataAndLog(indexInfo, fetchedDataList, worker, indexDataSyncRequest);
                resultLogs.addAll(logs);

            } catch (Exception e) {
                log.error("지수 데이터 연동 실패: {}", indexInfo.getIndexName(), e);
                SyncJob failLog = syncLogService.createFailureLog(
                        JobType.INDEX_DATA, worker, LocalDate.now(), indexInfo
                );
                resultLogs.add(syncJobMapper.toDto(failLog));
            }
        }

        return resultLogs;
    }


    @Transactional(readOnly = true)
    public CursorPageResponseSyncJobDto getSyncJobsByCursor(CursorPageRequestSyncJobDto request) {
        List<SyncJob> syncJobs = syncJobRepository.findAllByCursor(request);

        boolean hasNext = false;
        String nextCursor = request.cursor() != null ? request.cursor() : null;
        Long nextIdAfter = request.idAfter() != null ? request.idAfter() : null;
        Long totalElement = null;
        if (request.idAfter() == null && (request.cursor() == null || request.cursor().isBlank())) {
            totalElement = syncJobRepository.countByCursorFilter(request);
        }
        if (!syncJobs.isEmpty()) {
            if (syncJobs.size() > request.size()) {
                hasNext = true;
                syncJobs.remove(request.size());
            }

            SyncJob lastJob = syncJobs.get(syncJobs.size() - 1);

            nextIdAfter = lastJob.getId();
            if ("targetDate".equals(request.sortField())) {
                nextCursor = lastJob.getTargetDate().toString();
            } else if ("jobTime".equals(request.sortField())) {
                nextCursor = lastJob.getCreatedAt().toString();
            }else{
                nextCursor = lastJob.getCreatedAt().toString();
            }
        }

        List<SyncJobDto> content = syncJobs.stream()
                .map(syncJobMapper::toDto)
                .toList();

        return new CursorPageResponseSyncJobDto(
                content,
                nextCursor,
                nextIdAfter,
                request.size(),
                totalElement,
                hasNext);
    }

    public LocalDate getLastSyncDate(IndexInfo indexInfo){
        return syncJobRepository.findLatest(indexInfo, Result.SUCCESS)
                .map(SyncJob::getTargetDate)
                .orElse(null);
    }
}