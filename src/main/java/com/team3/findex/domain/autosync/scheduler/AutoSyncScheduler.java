package com.team3.findex.domain.autosync.scheduler;


import com.team3.findex.domain.autosync.AutoSync;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.service.SyncJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoSyncScheduler {

    private final AutoSyncService autoSyncService;
    private final SyncJobService syncJobService;

    @Transactional
    @Scheduled(cron = "${spring.batch.auto-sync.cron}")
    public void autoSyncSchedule(){
        log.info("지수 연동 배치 시작");

        List<AutoSync> enabledAutoSync = autoSyncService.getEnabledAutoSyncConfig();
        log.info("활성화된 연동 설정 수: {}", enabledAutoSync.size());

        if(enabledAutoSync.isEmpty()) {
            log.info("활성화된 연동 설정이 없습니다");
            return;
        }
        for(AutoSync autoSync : enabledAutoSync){
                syncForIndex(autoSync);
            }
        log.info("배치 완료");
    }


    private void syncForIndex(AutoSync autoSync){
        IndexInfo indexInfo = autoSync.getIndexInfo();
        log.info("지수 연동 시작 | 지수명: {}, 분류: {}",
                indexInfo.getIndexName(), indexInfo.getIndexClassification());

        LocalDate lastSyncDate = syncJobService.getLastSyncDate(indexInfo);
        LocalDate fromDate; //시작날짜
        LocalDate toDate = LocalDate.now(); //종료날짜
        if(lastSyncDate != null){
            fromDate = lastSyncDate.plusDays(1); // 마지막 다음 날짜
        } else {
            fromDate = LocalDate.now().minusDays(30); // 처음이면 최근 30일 날짜
        }

        List<Long> indexInfoIds = List.of(indexInfo.getId());

        IndexDataSyncRequest request = new IndexDataSyncRequest(
                indexInfoIds, fromDate.toString(), toDate.toString());

        log.info("연동 기간 | 시작: {}, 종료: {}", fromDate, toDate);

        syncJobService.syncIndexData(request, "System");

        log.info("지수 연동 완료 | 지수명: {}", indexInfo.getIndexName());
    }
}
