package com.team3.findex.domain.index.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.IndexInfoFindCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@RequiredArgsConstructor
@Repository
public class IndexInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<IndexInfo> findByCondition(Long cursorId, IndexInfoFindCondition condition, Pageable pageable) {

        return null;
    }

    private BooleanExpression cursorId(Long cursorId){
        return cursorId == null ? null : club.id.gt(cursorId);
    }
}
