package com.team3.findex.domain.autosync;

import static com.team3.findex.domain.autosync.QAutoSync.autoSync;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.findex.repository.AutoSyncRepositoryCustom;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * AutoSync 엔티티 대한 커스텀 레포지토리 구현체
 * <p>QueryDSL 사용하여 동적 쿼리 처리</p>
 */
@Repository
@RequiredArgsConstructor
public class AutoSyncRepositoryImpl implements AutoSyncRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    /**
     * AutoSync 목록 조회
     * <p>필터, 정렬 조건 적용하여 지정된 크기 만큼 데이터 조회</p>
     * <p>다음 페이지 존재 확인하기 위해 size+1개 조회</p>
     *
     * @param indexInfoId   ID 필터 (null 이면 전체조회)
     * @param enabled       활성화 상태
     * @param idAfter       이전 페이지 마지막 ID
     * @param cursor        문자열 반환, indexName 가질듯?
     * @param sortField     정렬 필드
     * @param sortDirection 정렬 방향
     * @param size          조회할 데이터 개수
     * @return 조회한 엔티티 리스트
     */
    @Override
    public List<AutoSync> findWithCursor(
            Long indexInfoId,
            Boolean enabled,
            Long idAfter,
            String cursor,
            String sortField,
            String sortDirection,
            int size) {
        return jpaQueryFactory
                .selectFrom(autoSync)
                .leftJoin(autoSync.indexInfo).fetchJoin()
                .where(
                        indexInfoIdEq(indexInfoId),
                        enabledEq(enabled),
                        idAfterEq(idAfter, sortDirection)
                )
                .orderBy(getOrderSpecifier(sortField, sortDirection))
                .limit(size + 1)
                .fetch();
    }

    /**
     * 필터 조건에 맞는 AutoSync 전체 개수를 조회
     *
     * @param indexInfoId IndexInfo ID
     * @param enabled     활성화 상태 필터
     * @return 전체 개수
     */

    @Override
    public long countWithFilter(Long indexInfoId, Boolean enabled) {
        Long count = jpaQueryFactory
                .select(autoSync.count())
                .from(autoSync)
                .where(
                        indexInfoIdEq(indexInfoId),
                        enabledEq(enabled)
                )
                .fetchOne();
        return count != null ? count : 0;
    }

    /**
     * IndexInfo ID 동적 필터 조건
     *
     * @param indexInfoId (null 이면 미적용)
     * @return BooleanExpression or null
     */
    private BooleanExpression indexInfoIdEq(Long indexInfoId) {
        if (indexInfoId == null) {
            return null;
        }
        return autoSync.indexInfo.id.eq(indexInfoId);
    }

    /**
     * enable 활성화 상태 필터 조건
     *
     * @param enabled 활성화 상태
     * @return BooleanExpression
     */
    private BooleanExpression enabledEq(Boolean enabled) {
        if (enabled == null) {
            return null;
        }
        return autoSync.isEnable.eq(enabled);
    }

    /**
     * 커서 기반 페이지네이션 ID 필터 조건
     * <li>ASC : id > idAfter (다음 페이지는 더 큰 id를 가짐)</li>
     * <li>DESC : id < adAfter (더 작은 id를 가짐)</li>
     *
     * @param idAfter       기준이 되는 ID
     * @param softDirection 정렬 방향 ("ASC"이면 > , "DESC"이면 < 사용)
     * @return BooleanExpression or null
     */
    private BooleanExpression idAfterEq(Long idAfter, String softDirection) {
        if (idAfter == null) {
            return null;
        }
        boolean desc = "DESC".equalsIgnoreCase(softDirection);
        return desc ? autoSync.id.lt(idAfter) : autoSync.id.gt(idAfter);
    }

    /**
     * 정렬 조건 생성
     *
     * @param sortField     정렬 필드 ("id", "indexInfo.indexName", "isEnable")
     * @param softDirection 정렬 방향 ("ASC", "DESC")
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> getOrderSpecifier(String sortField, String softDirection) {
        Order order = "DESC".equalsIgnoreCase(softDirection) ? Order.DESC : Order.ASC;

        // sortField 정렬 필드
        if ("indexInfo.indexName".equalsIgnoreCase(sortField)) {
            return new OrderSpecifier<>(order, autoSync.indexInfo.id);
        }

        if ("isEnable".equalsIgnoreCase(sortField)) {
            return new OrderSpecifier<>(order, autoSync.isEnable);
        }
        return new OrderSpecifier<>(order, autoSync.id);
    }

}
