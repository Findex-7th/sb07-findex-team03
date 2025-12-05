package com.team3.findex.domain.syncjob.service;

import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import com.team3.findex.domain.syncjob.openApiTester.OpenApiTester;
import com.team3.findex.domain.syncjob.openApiTester.mapper.OpenAPIMapper;
import com.team3.findex.repository.AutoSyncRepository;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SyncJobService {

    private final SyncJobRepository syncJobRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobMapper syncJobMapper;
    private final IndexDataRepository indexDataRepository;
    private final OpenApiTester openApiTester;
    private final OpenAPIMapper openAPIMapper;
    private final AutoSyncRepository autoSyncRepository;


    /**
     * 전체 지수 정보(Index Info)에 대한 동기화 작업을 수행하고 이력을 저장합니다.
     * <p>
     * 등록된 모든 지수 정보를 조회하여 순차적으로 연동 작업을 시도합니다.
     * 특정 지수 정보 처리 중 오류가 발생하거나 유효성 검증에 실패할 경우,
     * 전체 프로세스를 중단하지 않고 해당 건에 대해 '실패' 로그를 저장한 후 다음 작업을 계속 진행합니다.
     * </p>
     *
     * @param worker 작업을 수행하는 주체 (사용자의 Ip주소)
     * @return 수행된 모든 연동 작업의 결과 로그(SyncJobDto) 리스트 (성공 및 실패 포함)
     */
    @Transactional
    public List<SyncJobDto> syncIndexInfos(String worker){
        List<IndexInfo> indexInfos = openApiTester.fetchAllApi().getResponse().getBody().getItems().getItemList().stream()
                .map(openAPIMapper::toIndexInfoEntity)
                .toList();
//        indexInfoRepository.findByIndexClassificationAndIndexName()
        return indexInfos.stream()
                .map(indexInfo -> {
                    IndexInfo savedIndexInfo = indexInfoRepository.findByIndexClassificationAndIndexName(
                            indexInfo.getIndexClassification(),
                            indexInfo.getIndexName()
                    ).map(existing -> {
                        existing.update(
                                indexInfo.getEmployedItemsCount(),
                                indexInfo.getBasePointInTime(),
                                indexInfo.getBaseIndex(),
                                indexInfo.getFavorite()
                        );
                        return existing;
                    }).orElseGet(() -> {
                        return indexInfoRepository.save(indexInfo);
                    });
                    return createSuccessLog(JobType.INDEX_INFO, worker, null, savedIndexInfo);
                })
                .map(syncJobMapper::toDto)
                .toList();
    }

    @Transactional
    public List<SyncJobDto> syncIndexData(
            IndexDataSyncRequest indexDataSyncRequest,
            String worker
            ){
        List<Long> indexInfoIds = indexDataSyncRequest.indexInfoIds();
        List<IndexData> indexDataList = indexDataRepository.findAll(); /*indexDataRepository.findAllByIdInAndBaseDateBetween(indexInfoIds, indexDataSyncRequest.baseDateFrom(), indexDataSyncRequest.baseDateTo());*/
        return indexDataList.stream().map(indexData -> {
            if(indexData.getIndexInfo() == null) {
                log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexData.getIndexInfo().getId(), "지수 정보를 확인 할 수 없습니다.");
                return createFailureLog(JobType.INDEX_INFO, worker, indexData.getBaseDate(), indexData.getIndexInfo());
            }
            if(worker == null || worker.isBlank()){
                log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexData.getIndexInfo().getId(), "작업자를 확인 할 수 없습니다.");
                return createFailureLog(JobType.INDEX_INFO, worker, indexData.getBaseDate(), indexData.getIndexInfo());
            }
            return createSuccessLog(JobType.INDEX_INFO, worker, indexData.getBaseDate(), indexData.getIndexInfo());
        })
                .map(syncJobMapper::toDto)
                .toList();
    }

    public CursorPageResponseSyncJobDto getSyncJobsByCursor(CursorPageRequestSyncJobDto request){
        List<SyncJob> syncJobs = syncJobRepository.findAllByCursor(request);
        boolean hasNext = false;
        String nextCursor = null;
        Long nextIdAfter = null;
        if(!syncJobs.isEmpty()){
            SyncJob lastJob = syncJobs.get(syncJobs.size() - 1);
            if(syncJobs.size() > request.size()){
                hasNext = true;
                syncJobs.remove(request.size());
                nextIdAfter = lastJob.getId();
            }
            nextCursor = String.valueOf(lastJob.getCreatedAt());
            if ("targetDate".equals(request.sortField())) {
                nextCursor = null;
            } else if ("jobTime".equals(request.sortField())) {
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
                (long) content.size(),
                hasNext);
    }

    /**
     * [내부 메서드] 작업 성공 상태의 로그를 생성하고 저장합니다.
     *
     * @param jobType 작업 유형 (INDEX_INFO: 지수 정보, INDEX_DATA: 지수 데이터)
     * @param worker 작업 수행자
     * @param indexInfo 대상 지수 정보 엔티티
     * @return DB에 저장된 성공 상태(Result.SUCCESS)의 SyncJob 엔티티
     */
    @Transactional
    protected SyncJob createSuccessLog(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo) {
        SyncJob successJob = SyncJob.ofSuccess(jobType, worker, targetDate, indexInfo);
         return syncJobRepository.save(successJob);
    }

    /**
     * [내부 메서드] 작업 실패 상태의 로그를 생성하고 저장합니다.
     * <p>
     * 예외 발생 시 호출되며, 트랜잭션 롤백 없이 실패 이력을 남기기 위해 사용됩니다.
     * </p>
     *
     * @param jobType 작업 유형 (INDEX_INFO: 지수 정보, INDEX_DATA: 지수 데이터)
     * @param worker 작업 수행자
     * @param indexInfo 대상 지수 정보 엔티티
     * @return DB에 저장된 실패 상태(Result.FAILED)의 SyncJob 엔티티
     */
    @Transactional
    protected SyncJob createFailureLog(JobType jobType, String worker, LocalDate targetDate, IndexInfo indexInfo) {
        SyncJob failJob = SyncJob.ofFailure(jobType, worker, targetDate, indexInfo);
        return syncJobRepository.save(failJob);
    }
}