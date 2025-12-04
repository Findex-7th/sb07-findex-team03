package com.team3.findex.domain.syncjob.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.syncjob.SyncJob;
import com.team3.findex.domain.syncjob.dto.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.enums.JobType;
import com.team3.findex.domain.syncjob.enums.Result;
import com.team3.findex.domain.syncjob.repository.SyncJobRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;


import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.team3.findex.domain.syncjob.QSyncJob.syncJob;

@RequiredArgsConstructor
public class SyncJobRepositoryImpl implements SyncJobRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SyncJob> findAllByCursor(CursorPageRequestSyncJobDto request) {
        return null;
    }
}
