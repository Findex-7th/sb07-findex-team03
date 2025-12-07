package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.repository.AutoSyncRepository;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.repository.IndexDataRepository;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import com.team3.findex.domain.syncjob.infrastructure.openapitester.OpenApiTester;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncJobService {

    private final SyncJobRepository syncJobRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobMapper syncJobMapper;
    private final IndexDataRepository indexDataRepository;
    private final OpenApiTester openApiTester;
    private final AutoSyncRepository autoSyncRepository;
    private final SyncLogService syncLogService;

    @Autowired
    @Lazy
    private SyncJobService self;


    public List<SyncJobDto> syncIndexInfos(String worker) {
        List<IndexInfo> fetchedIndexInfos = openApiTester.fetchAllApiToIndexInfo();

        List<SyncJobDto> resultLogs = new ArrayList<>();

        if (fetchedIndexInfos.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return self.bulkSaveIndexInfosAndLog(fetchedIndexInfos, worker);

        } catch (Exception e) {
            log.error("지수 정보 일괄 동기화 실패", e);

            return Collections.emptyList();
        }
    }

@Transactional
public List<SyncJobDto> bulkSaveIndexInfosAndLog(List<IndexInfo> fetchedInfos, String worker) {
    List<IndexInfo> existingInfos = indexInfoRepository.findAll();

    Map<String, IndexInfo> existingMap = existingInfos.stream()
            .collect(Collectors.toMap(
                    info -> info.getIndexClassification() + "|" + info.getIndexName(),
                    Function.identity()
            ));

    List<IndexInfo> newInfosToSave = new ArrayList<>();
    List<IndexInfo> allProcessedInfos = new ArrayList<>();

    for (IndexInfo fetched : fetchedInfos) {
        String key = fetched.getIndexClassification() + "|" + fetched.getIndexName();
        if (existingMap.containsKey(key)) {
            IndexInfo existing = existingMap.get(key);
            existing.update(
                    fetched.getEmployedItemsCount(),
                    fetched.getBasePointInTime(),
                    fetched.getBaseIndex(),
                    null
            );
            allProcessedInfos.add(existing);
        } else {
            newInfosToSave.add(fetched);
            allProcessedInfos.add(fetched);
        }
    }
    if (!newInfosToSave.isEmpty()) {
        indexInfoRepository.saveAll(newInfosToSave);
    }

    List<AutoSync> existingAutoSyncs = autoSyncRepository.findByIndexInfoIn(allProcessedInfos);

    Set<Long> linkedIndexInfoIds = existingAutoSyncs.stream()
            .map(autoSync -> autoSync.getIndexInfo().getId())
            .collect(Collectors.toSet());

    List<AutoSync> newAutoSyncs = new ArrayList<>();

    for (IndexInfo info : allProcessedInfos) {
        if (!linkedIndexInfoIds.contains(info.getId())) {
            newAutoSyncs.add(new AutoSync(info));
        }
    }
    if (!newAutoSyncs.isEmpty()) {
        autoSyncRepository.saveAll(newAutoSyncs);
    }


    List<SyncJob> logsToSave = allProcessedInfos.stream()
            .map(info -> SyncJob.ofSuccess(
                    JobType.INDEX_INFO,
                    worker,
                    info.getBasePointInTime(),
                    info
            ))
            .collect(Collectors.toList());
    syncLogService.saveAllSuccessLogs(logsToSave);

    return logsToSave.stream()
            .map(syncJobMapper::toDto)
            .collect(Collectors.toList());
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
                List<IndexData> fetchedDataList = openApiTester.fetchApiByParamsToIndexData(
                        indexInfo.getIndexName(),
                        indexDataSyncRequest.baseDateFrom().replace("-", ""),
                        indexDataSyncRequest.baseDateTo().replace("-", ""),
                        indexInfo
                );

                if (fetchedDataList.isEmpty()) continue;

                List<SyncJobDto> logs = self.bulkSaveIndexDataAndLog(indexInfo, fetchedDataList, worker, indexDataSyncRequest);
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

    @Transactional
    public List<SyncJobDto> bulkSaveIndexDataAndLog(IndexInfo indexInfo, List<IndexData> fetchedDataList, String worker, IndexDataSyncRequest indexDataSyncRequest) {
        LocalDate baseDateFrom = (indexDataSyncRequest.baseDateFrom() == null || indexDataSyncRequest.baseDateFrom().isEmpty())
                ? fetchedDataList.stream().map(IndexData::getBaseDate).min(LocalDate::compareTo).orElse(LocalDate.MIN)
                : LocalDate.parse(indexDataSyncRequest.baseDateFrom());
        LocalDate baseDateTo = (indexDataSyncRequest.baseDateTo() == null || indexDataSyncRequest.baseDateTo().isEmpty())
                ? fetchedDataList.stream().map(IndexData::getBaseDate).max(LocalDate::compareTo).orElse(LocalDate.MAX)
                : LocalDate.parse(indexDataSyncRequest.baseDateTo());
        List<IndexData> existingDataList = indexDataRepository.findAllByIndexInfoAndBaseDateBetween(indexInfo, baseDateFrom, baseDateTo);

        Map<LocalDate, IndexData> existingMap = existingDataList.stream()
                .collect(Collectors.toMap(IndexData::getBaseDate, Function.identity()));

        List<IndexData> dataToSave = new ArrayList<>();
        List<SyncJob> logsToSave = new ArrayList<>();
        for (IndexData fetched : fetchedDataList) {
            if (existingMap.containsKey(fetched.getBaseDate())) {
                existingMap.get(fetched.getBaseDate()).updateFromSync(fetched);
            }
            else {
                dataToSave.add(fetched);
            }

            SyncJob successJob = SyncJob.ofSuccess(
                    JobType.INDEX_DATA,
                    worker,
                    fetched.getBaseDate(),
                    indexInfo
            );
            logsToSave.add(successJob);
        }
        if (!dataToSave.isEmpty()) {
            indexDataRepository.saveAll(dataToSave);
        }
        syncLogService.saveAllSuccessLogs(logsToSave);
        return logsToSave.stream()
                .map(syncJobMapper::toDto)
                .toList();
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