package com.team3.findex.domain.index.repository.custom.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.dto.ChartDataPointDto;
import com.team3.findex.domain.index.dto.IndexDataFindCondition;
import com.team3.findex.domain.index.dto.IndexDataFindSort;
import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.domain.index.repository.custom.IndexDataRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public Long CountByCondition(IndexDataFindCondition condition) {
        return queryFactory.select(indexData.count())
                .from(indexData)
                .where(
                        indexInfoIdEq(condition.indexInfoId()),
                        baseDateGoe(condition.startDate()),
                        baseDateLoe(condition.endDate())
                )
                .fetchOne();
    }

    @Override
    public List<ChartDataPointDto> findChartData(Long indexInfoId, ChartPeriodType periodType, int avgAmount) {

        LocalDate lastValidDate = queryFactory
                .select(indexData.baseDate)
                .from(indexData)
                .where(indexInfoIdEq(indexInfoId))
                .orderBy(indexData.baseDate.desc())
                .offset(avgAmount - 1)
                .limit(1)
                .fetchOne();
        if (lastValidDate == null) {
            return Collections.emptyList();
        }

        LocalDate startDate = calculateStartDate(periodType);

        NumberTemplate<Double> movingAvgPath = Expressions.numberTemplate(
                Double.class,
                "AVG({0}) OVER (ORDER BY {1} ASC ROWS BETWEEN CURRENT ROW AND {2} FOLLOWING)",
                indexData.closingPrice,
                indexData.baseDate,
                avgAmount - 1
        );
        List<Tuple> result = queryFactory
                .select(
                        indexData.baseDate,
                        movingAvgPath
                )
                .from(indexData)
                .where(
                        indexInfoIdEq(indexInfoId),
                        indexData.baseDate.goe(startDate),
                        indexData.baseDate.loe(lastValidDate)
                )
                .orderBy(indexData.baseDate.desc())
                .fetch();
        return result.stream()
                .map(tuple -> new ChartDataPointDto(
                        Objects.requireNonNull(tuple.get(indexData.baseDate)).toString(),
                        tuple.get(movingAvgPath)
                ))
                .collect(Collectors.toList());
    }


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

    private LocalDate calculateStartDate(ChartPeriodType periodType) {
        LocalDate now = LocalDate.now();
        if (periodType == null) return now.minusMonths(3); // 기본값

        return switch (periodType) {
            case YEARLY -> now.minusYears(1);
            case QUARTERLY -> now.minusMonths(3);
            case MONTHLY -> now.minusMonths(1);
            // 필요에 따라 케이스 추가
            default -> now.minusYears(1);
        };
    }
}
