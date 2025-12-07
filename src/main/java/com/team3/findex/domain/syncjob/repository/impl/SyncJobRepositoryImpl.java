package com.team3.findex.domain.syncjob.repository.impl;

import static com.team3.findex.domain.syncjob.QSyncJob.syncJob;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.syncjob.QSyncJob;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.repository.SyncJobRepository;
import com.team3.findex.domain.syncjob.repository.SyncJobRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SyncJobRepositoryImpl implements SyncJobRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SyncJob> findAllByCursor(CursorPageRequestSyncJobDto request) {
        LocalDate targetDateFrom = StringUtils.hasText(request.baseDateFrom()) ? LocalDate.parse(request.baseDateFrom()) : null;
        LocalDate targetDateTo = StringUtils.hasText(request.baseDateTo()) ? LocalDate.parse(request.baseDateTo()) : null;

        Instant jobTimeFrom = StringUtils.hasText(request.jobTimeFrom()) ? Instant.parse(request.jobTimeFrom()) : null;
        Instant jobTimeTo = StringUtils.hasText(request.jobTimeTo()) ? Instant.parse(request.jobTimeTo()) : null;

        System.out.println("request = " + request);
        
        Result resultStatus = null;
        if (StringUtils.hasText(request.status())) {
            try {
                resultStatus = Result.valueOf(request.status());
            } catch (IllegalArgumentException ignored) {}
        }

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        BooleanExpression cursorCondition = null;
        boolean isAsc = "ASC".equalsIgnoreCase(request.sortDirection());
        boolean hasCursor = StringUtils.hasText(request.cursor());
        boolean hasIdAfter = request.idAfter() != null;

        if ("targetDate".equals(request.sortField())) {
            orders.add(isAsc ? syncJob.targetDate.asc() : syncJob.targetDate.desc());
            orders.add(syncJob.id.desc());

            if (hasCursor && hasIdAfter) {
                LocalDate cursorDate = LocalDate.parse(request.cursor());
                if (isAsc) {
                    cursorCondition = syncJob.targetDate.gt(cursorDate)
                            .or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.lt(request.idAfter())));
                } else {
                    cursorCondition = syncJob.targetDate.lt(cursorDate)
                            .or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.lt(request.idAfter())));
                }
            }
        }
        // B. 정렬 기준: JobTime (Instant)
        else if ("jobTime".equals(request.sortField())) {
            orders.add(isAsc ? syncJob.createdAt.asc() : syncJob.createdAt.desc());
            orders.add(syncJob.id.desc());
            if (hasCursor && hasIdAfter) {
                Instant cursorTime = Instant.parse(request.cursor());
                if (isAsc) {
                    cursorCondition = syncJob.createdAt.gt(cursorTime)
                            .or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.lt(request.idAfter())));
                } else {
                    cursorCondition = syncJob.createdAt.lt(cursorTime)
                            .or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.lt(request.idAfter())));
                }
            }
        }
        else {
            orders.add(syncJob.id.desc());
            if (hasIdAfter) {
                cursorCondition = syncJob.id.lt(request.idAfter());
            }
        }

        List<SyncJob> returnData = queryFactory
                .selectFrom(syncJob)
                .where(
                        request.jobType() != null ? syncJob.jobType.eq(request.jobType()) : null,

                        request.indexInfoId() != null ? syncJob.indexInfo.id.eq(request.indexInfoId()) : null,

                        StringUtils.hasText(request.worker()) ? syncJob.worker.eq(request.worker()) : null,

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
                .limit(request.size() + 1)
                .fetch();

        returnData.forEach(data -> {
            System.out.println("data = " + data);
        });
        return returnData;
    }

    @Override
    public Long countByCursorFilter(CursorPageRequestSyncJobDto request) {

        LocalDate targetDateFrom = StringUtils.hasText(request.baseDateFrom()) ? LocalDate.parse(request.baseDateFrom()) : null;
        LocalDate targetDateTo = StringUtils.hasText(request.baseDateTo()) ? LocalDate.parse(request.baseDateTo()) : null;

        Instant jobTimeFrom = StringUtils.hasText(request.jobTimeFrom()) ? Instant.parse(request.jobTimeFrom()) : null;
        Instant jobTimeTo = StringUtils.hasText(request.jobTimeTo()) ? Instant.parse(request.jobTimeTo()) : null;

        Result resultStatus = null;
        if (StringUtils.hasText(request.status())) {
            try {
                resultStatus = Result.valueOf(request.status());
            } catch (IllegalArgumentException ignored) {}
        }

        return queryFactory
                .select(syncJob.count())
                .from(syncJob)
                .where(
                        request.jobType() != null ? syncJob.jobType.eq(request.jobType()) : null,

                        request.indexInfoId() != null ? syncJob.indexInfo.id.eq(request.indexInfoId()) : null,

                        StringUtils.hasText(request.worker()) ? syncJob.worker.eq(request.worker()) : null,

                        resultStatus != null ? syncJob.result.eq(resultStatus) : null,

                        (targetDateFrom != null && targetDateTo != null) ? syncJob.targetDate.between(targetDateFrom, targetDateTo) :
                                (targetDateFrom != null ? syncJob.targetDate.goe(targetDateFrom) :
                                        (targetDateTo != null ? syncJob.targetDate.loe(targetDateTo) : null)),

                        (jobTimeFrom != null && jobTimeTo != null) ? syncJob.createdAt.between(jobTimeFrom, jobTimeTo) :
                                (jobTimeFrom != null ? syncJob.createdAt.goe(jobTimeFrom) :
                                        (jobTimeTo != null ? syncJob.createdAt.loe(jobTimeTo) : null))
                )
                .fetchOne();
    }
}
