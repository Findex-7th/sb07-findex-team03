package com.team3.findex.domain.syncjob.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.syncjob.QSyncJob;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.repository.SyncJobRepositoryCustom;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.team3.findex.domain.syncjob.QSyncJob.syncJob;

@RequiredArgsConstructor
public class SyncJobRepositoryImpl implements SyncJobRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SyncJob> findAllByCursor(CursorPageRequestSyncJobDto dto) {
        QSyncJob syncJob = QSyncJob.syncJob;

        LocalDate targetDateFrom = StringUtils.hasText(dto.baseDateFrom()) ? LocalDate.parse(dto.baseDateFrom()) : null;
        LocalDate targetDateTo = StringUtils.hasText(dto.baseDateTo()) ? LocalDate.parse(dto.baseDateTo()) : null;

        Instant jobTimeFrom = StringUtils.hasText(dto.jobTimeFrom()) ? Instant.parse(dto.jobTimeFrom()) : null;
        Instant jobTimeTo = StringUtils.hasText(dto.jobTimeTo()) ? Instant.parse(dto.jobTimeTo()) : null;

        Result resultStatus = null;
        if (StringUtils.hasText(dto.status())) {
            try {
                resultStatus = Result.valueOf(dto.status());
            } catch (IllegalArgumentException ignored) {}
        }

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        BooleanExpression cursorCondition = null;
        boolean isAsc = "ASC".equalsIgnoreCase(dto.sortDirection());

        if ("targetDate".equals(dto.sortField())) {
            orders.add(isAsc ? syncJob.targetDate.asc() : syncJob.targetDate.desc());
            orders.add(syncJob.id.asc()); // 2차 정렬 (ID)

            if (StringUtils.hasText(dto.cursor()) && dto.idAfter() != null) {
                LocalDate cursorDate = LocalDate.parse(dto.cursor());
                cursorCondition = isAsc
                        ? syncJob.targetDate.gt(cursorDate).or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.gt(dto.idAfter())))
                        : syncJob.targetDate.lt(cursorDate).or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.gt(dto.idAfter())));
            }
        }
        else if ("jobTime".equals(dto.sortField())) {
            orders.add(isAsc ? syncJob.createdAt.asc() : syncJob.createdAt.desc());
            orders.add(syncJob.id.asc());

            if (StringUtils.hasText(dto.cursor()) && dto.idAfter() != null) {
                Instant cursorTime = Instant.parse(dto.cursor());
                cursorCondition = isAsc
                        ? syncJob.createdAt.gt(cursorTime).or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.gt(dto.idAfter())))
                        : syncJob.createdAt.lt(cursorTime).or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.gt(dto.idAfter())));
            }
        }
        else {
            orders.add(syncJob.id.asc());
            if (dto.idAfter() != null) {
                cursorCondition = syncJob.id.gt(dto.idAfter());
            }
        }
        return queryFactory
                .selectFrom(syncJob)
                .where(
                        dto.jobType() != null ? syncJob.jobType.eq(dto.jobType()) : null,

                        dto.indexInfoId() != null ? syncJob.indexInfo.id.eq(dto.indexInfoId()) : null,

                        StringUtils.hasText(dto.worker()) ? syncJob.worker.eq(dto.worker()) : null,

                        resultStatus != null ? syncJob.result.eq(resultStatus) : null,

                        (targetDateFrom != null && targetDateTo != null) ? syncJob.targetDate.between(targetDateFrom, targetDateTo) :
                                (targetDateFrom != null ? syncJob.targetDate.goe(targetDateFrom) :
                                        (targetDateTo != null ? syncJob.targetDate.loe(targetDateTo) : null)),

                        (jobTimeFrom != null && jobTimeTo != null) ? syncJob.createdAt.between(jobTimeFrom, jobTimeTo) :
                                (jobTimeFrom != null ? syncJob.createdAt.goe(jobTimeFrom) :
                                        (jobTimeTo != null ? syncJob.createdAt.loe(jobTimeTo) : null)),

                        cursorCondition
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .limit(dto.size())
                .fetch();
    }
}
