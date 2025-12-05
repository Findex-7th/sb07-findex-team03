package com.team3.findex.domain.syncjob.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.syncjob.QSyncJob;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.repository.SyncJobRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.team3.findex.domain.syncjob.QSyncJob.syncJob;

@RequiredArgsConstructor
public class SyncJobRepositoryImpl implements SyncJobRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SyncJob> findAllByCursor(CursorPageRequestSyncJobDto request) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSyncJob syncJob = QSyncJob.syncJob;

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

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        BooleanExpression cursorCondition = null;
        boolean isAsc = "ASC".equalsIgnoreCase(request.sortDirection());

        if ("targetDate".equals(request.sortField())) {
            orders.add(syncJob.id.desc());
            orders.add(isAsc ? syncJob.targetDate.asc() : syncJob.targetDate.desc());

            if (StringUtils.hasText(request.cursor()) && request.idAfter() != null) {
                LocalDate cursorDate = LocalDate.parse(request.cursor());
                cursorCondition = isAsc
                        ? syncJob.targetDate.gt(cursorDate).or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.gt(request.idAfter())))
                        : syncJob.targetDate.lt(cursorDate).or(syncJob.targetDate.eq(cursorDate).and(syncJob.id.gt(request.idAfter())));
            }
        }
        else if ("jobTime".equals(request.sortField())) {
            orders.add(syncJob.id.desc());
            orders.add(isAsc ? syncJob.createdAt.asc() : syncJob.createdAt.desc());

            if (StringUtils.hasText(request.cursor()) && request.idAfter() != null) {
                Instant cursorTime = Instant.parse(request.cursor());
                cursorCondition = isAsc
                        ? syncJob.createdAt.gt(cursorTime).or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.gt(request.idAfter())))
                        : syncJob.createdAt.lt(cursorTime).or(syncJob.createdAt.eq(cursorTime).and(syncJob.id.gt(request.idAfter())));
            }
        }
        else {
            orders.add(syncJob.id.desc());
        }
        if (request.idAfter() != null) {
            cursorCondition = syncJob.id.goe(request.idAfter());
        }
        return queryFactory
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
    }

    @Override
    public Long countByCursorFilter(CursorPageRequestSyncJobDto request) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSyncJob syncJob = QSyncJob.syncJob;

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
                                        (jobTimeTo != null ? syncJob.createdAt.loe(jobTimeTo) : null))
                )
                .stream().count();
    }
}
