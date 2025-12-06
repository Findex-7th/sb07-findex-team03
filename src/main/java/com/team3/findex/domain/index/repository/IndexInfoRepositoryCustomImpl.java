package com.team3.findex.domain.index.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.IndexInfoFindCondition;
import com.team3.findex.domain.index.dto.IndexInfoFindSort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.team3.findex.domain.index.QIndexInfo.indexInfo;

@RequiredArgsConstructor
@Repository
public class IndexInfoRepositoryCustomImpl implements IndexInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<IndexInfo> findWithCursor(Long cursor, int size, IndexInfoFindSort sort) {
        return queryFactory.selectFrom(indexInfo)
                .where(cursorIdGt(cursor))
                .orderBy(indexInfo.indexClassification.asc()) // TODO 나중에 바꾸기. 지금은 기본
                .limit(size + 1)
                .fetch();
    }


    @Override
    public List<IndexInfo> findByCondition(Long cursor, IndexInfoFindCondition condition, int size, IndexInfoFindSort sort) {
        return null;
    }

    private BooleanExpression cursorIdGt(Long cursorId) {
        return cursorId == null ? null : indexInfo.id.gt(cursorId);
    }

    private BooleanExpression indexClassificationContains(String indexClassification) {
        return indexClassification == null ? null : indexInfo.indexClassification.containsIgnoreCase(indexClassification);
    }

    private BooleanExpression indexClassificationEq(String indexClassification) {
        if (indexClassification == null) {
            return null;
        }
        return indexInfo.indexClassification.eq(indexClassification);
    }

    private BooleanExpression indexNameEq(String indexName) {
        if (indexName == null) {
            return null;
        }
        return indexInfo.indexName.eq(indexName);
    }

    private BooleanExpression favoriteEq(Boolean isFavorite) {
        if (isFavorite == null) {
            return null;
        }
        return indexInfo.favorite.eq(isFavorite);
    }
}
