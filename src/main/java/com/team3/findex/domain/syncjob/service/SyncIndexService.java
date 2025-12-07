package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.repository.AutoSyncRepository;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.repository.IndexDataRepository;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SyncIndexService {
    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncRepository autoSyncRepository;
    private final SyncJobMapper syncJobMapper;
    private final SyncLogService syncLogService;
    private final IndexDataRepository indexDataRepository;

    public List<SyncJobDto> bulkSaveIndexInfosAndLog(List<IndexInfo> fetchedInfos, String worker) {

        if (fetchedInfos == null || fetchedInfos.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> indexClassifications = fetchedInfos.stream().map(IndexInfo::getIndexClassification).distinct().toList();
        List<String> indexNames = fetchedInfos.stream().map(IndexInfo::getIndexName).distinct().toList();

        List<IndexInfo> existingInfos = indexInfoRepository.findByIndexClassificationInAndIndexNameIn(indexClassifications, indexNames);


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

    public List<SyncJobDto> bulkSaveIndexDataAndLog(IndexInfo indexInfo, List<IndexData> fetchedDataList, String worker, IndexDataSyncRequest indexDataSyncRequest) {

        if (fetchedDataList == null || fetchedDataList.isEmpty()) {
            return Collections.emptyList();
        }

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
}
