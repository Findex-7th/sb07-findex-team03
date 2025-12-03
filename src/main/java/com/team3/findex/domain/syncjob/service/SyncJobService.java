package com.team3.findex.domain.syncjob.service;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.dto.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.SyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.mapper.SyncJobMapper;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.repository.SyncJobRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
        List<IndexInfo> indexInfos = indexInfoRepository.findAll();
        return indexInfos.stream()
                .map(indexInfo -> {
                    if(worker == null || worker.isBlank()){
                        log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexInfo.getId(), "작업자를 확인 할 수 없습니다.");
                        return createFailureLog(JobType.INDEX_INFO, worker, indexInfo);
                    }
                    if(indexInfo == null) {
                        log.error("SyncJob 생성 실패 - IndexInfo ID: {}, 에러: {}", indexInfo.getId(), "지수 정보를 확인 할 수 없습니다.");
                        return createFailureLog(JobType.INDEX_INFO, worker, indexInfo);
                    }
                    return createSuccessLog(JobType.INDEX_INFO, worker, indexInfo);
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
//        List<IndexData> indexDataList = indexDataRepository.findAllByIdInAndBaseDateBetween(indexInfoIds, indexDataSyncRequest.baseDateFrom(), indexDataSyncRequest.baseDateTo());

        return null;
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
    protected SyncJob createSuccessLog(JobType jobType, String worker, IndexInfo indexInfo) {
        SyncJob successJob = SyncJob.ofSuccess(jobType, worker, indexInfo);
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
    protected SyncJob createFailureLog(JobType jobType, String worker, IndexInfo indexInfo) {
        SyncJob failJob = SyncJob.ofFailure(jobType, worker, indexInfo);
        return syncJobRepository.save(failJob);
    }
}