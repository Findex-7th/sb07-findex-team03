package com.team3.findex.domain.index.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.dto.IndexDataFindCondition;
import com.team3.findex.domain.index.dto.IndexDataFindSort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.team3.findex.domain.index.QIndexData.indexData;

@RequiredArgsConstructor
@Repository
public class IndexDataRepositoryCustomImpl implements IndexDataRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<IndexData> findByCondition(Long idAfter, IndexDataFindCondition condition, int size, IndexDataFindSort sort) {
        return queryFactory.selectFrom(indexData)
                .where(
                        indexInfoIdEq(condition.indexInfoId()),
                        baseDateGoe(condition.startDate()),
                        baseDateLoe(condition.endDate())
                )
                .orderBy(createOrderSpecifier(sort))
                .offset(idAfter == null ? 0 : idAfter)
                .limit(size + 1)
                .fetch();
    }

    // ========== 정렬 변환 메서드 ==========

    /**
     * IndexDataFindSort를 QueryDSL OrderSpecifier로 변환
     *
     * @param sort 정렬 조건
     * @return QueryDSL OrderSpecifier
     */
    private OrderSpecifier<?> createOrderSpecifier(IndexDataFindSort sort) {
        boolean isAsc = sort.order().isAscending();

        return switch (sort.sortField()) {
            case BASE_DATE -> isAsc
                ? indexData.baseDate.asc()
                : indexData.baseDate.desc();

            case MARKET_PRICE -> isAsc
                ? indexData.marketPrice.asc()
                : indexData.marketPrice.desc();

            case CLOSING_PRICE -> isAsc
                ? indexData.closingPrice.asc()
                : indexData.closingPrice.desc();

            case HIGH_PRICE -> isAsc
                ? indexData.highPrice.asc()
                : indexData.highPrice.desc();

            case LOW_PRICE -> isAsc
                ? indexData.lowPrice.asc()
                : indexData.lowPrice.desc();

            case VERSUS -> isAsc
                ? indexData.versus.asc()
                : indexData.versus.desc();

            case FLUCTUATION_RATE -> isAsc
                ? indexData.fluctuationRate.asc()
                : indexData.fluctuationRate.desc();

            case TRADING_QUANTITY -> isAsc
                ? indexData.tradingQuantity.asc()
                : indexData.tradingQuantity.desc();

            case TRADING_PRICE -> isAsc
                ? indexData.tradingPrice.asc()
                : indexData.tradingPrice.desc();

            case MARKET_TOTAL_AMOUNT -> isAsc
                ? indexData.marketTotalAmount.asc()
                : indexData.marketTotalAmount.desc();

            case ID -> isAsc
                ? indexData.id.asc()
                : indexData.id.desc();
        };
    }

    // ========== 동적 조건 메서드들 ==========

    /**
     * 커서 ID보다 큰 값 필터링
     *
     * @param cursorId 커서 ID (null이면 조건 무시)
     * @return BooleanExpression
     */
    private BooleanExpression cursorIdGt(Long cursorId) {
        return cursorId == null ? null : indexData.id.gt(cursorId);
    }

    /**
     * 지수 ID 일치 필터링
     *
     * @param indexInfoId 지수 ID (null이면 조건 무시)
     * @return BooleanExpression
     */
    private BooleanExpression indexInfoIdEq(Long indexInfoId) {
        return indexInfoId == null ? null : indexData.indexInfo.id.eq(indexInfoId);
    }

    /**
     * 시작 날짜 이상 필터링 (Greater or Equal)
     *
     * @param startDate 시작 날짜 (null이면 조건 무시)
     * @return BooleanExpression
     */
    private BooleanExpression baseDateGoe(LocalDate startDate) {
        return startDate == null ? null : indexData.baseDate.goe(startDate);
    }

    /**
     * 종료 날짜 이하 필터링 (Less or Equal)
     *
     * @param endDate 종료 날짜 (null이면 조건 무시)
     * @return BooleanExpression
     */
    private BooleanExpression baseDateLoe(LocalDate endDate) {
        return endDate == null ? null : indexData.baseDate.loe(endDate);
    }
}
