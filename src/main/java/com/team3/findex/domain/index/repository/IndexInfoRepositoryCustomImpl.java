package com.team3.findex.domain.index.repository;

import com.querydsl.core.types.OrderSpecifier;
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
    public List<IndexInfo> findByCondition(Long idAfter, IndexInfoFindCondition condition, int size, IndexInfoFindSort sort) {
        return queryFactory.selectFrom(indexInfo)
                .where(
                        indexClassificationContains(condition.indexClassification()),
                        indexNameContains(condition.indexName()),
                        favoriteEq(condition.isFavorite())
                )
                .orderBy(orderSpecifier(sort), indexInfo.id.asc())
                .offset(idAfter == null ? 0 : idAfter)
                .limit(size + 1)
                .fetch();
    }

    @Override
    public Long CountByCondition(IndexInfoFindCondition condition) {
        return queryFactory
                .select(indexInfo.count())
                .from(indexInfo)
                .where(
                        indexClassificationContains(condition.indexClassification()),
                        indexNameContains(condition.indexName()),
                        favoriteEq(condition.isFavorite())
                ).fetchOne();
    }

    private OrderSpecifier<?> orderSpecifier(IndexInfoFindSort sort) {
        if (sort == null) {
            return indexInfo.indexClassification.asc();
        }

        boolean isAsc = sort.sortDirection().isAscending();

        return switch (sort.indexInfoSortField()) {
            case INDEX_CLASSIFICATION ->
                    isAsc ? indexInfo.indexClassification.asc() : indexInfo.indexClassification.desc();
            case INDEX_NAME -> isAsc ? indexInfo.indexName.asc() : indexInfo.indexName.desc();
            case EMPLOYED_ITEMS_COUNT ->
                    isAsc ? indexInfo.employedItemsCount.asc() : indexInfo.employedItemsCount.desc();
            case ID -> isAsc ? indexInfo.id.asc() : indexInfo.id.desc();
            default -> indexInfo.indexClassification.asc();
        };
    }

    private BooleanExpression indexClassificationContains(String indexClassification) {
        return indexClassification == null ?
                null : indexInfo.indexClassification.containsIgnoreCase(indexClassification);
    }

    private BooleanExpression indexNameContains(String indexName) {
        if (indexName == null) {
            return null;
        }
        return indexInfo.indexName.containsIgnoreCase(indexName);
    }

    private BooleanExpression favoriteEq(Boolean isFavorite) {
        if (isFavorite == null) {
            return null;
        }
        return indexInfo.favorite.eq(isFavorite);
    }
}
