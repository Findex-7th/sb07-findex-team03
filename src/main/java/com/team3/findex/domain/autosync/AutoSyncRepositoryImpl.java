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

@Repository
@RequiredArgsConstructor
public class AutoSyncRepositoryImpl implements AutoSyncRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<AutoSync> findWithCursor(
      Long indexInfoId,
      boolean enabled,
      Long idAfter,
      String sortField,
      String sortDirection,
      int size) {
    return jpaQueryFactory
        .selectFrom(autoSync)
        .leftJoin(autoSync.indexInfo).fetchJoin()
        .where(
            indexInfoIdEq(indexInfoId),
            enabledEq(enabled),
            idAfterEq(idAfter)
        )
        .orderBy(getOrderSpecifier(sortField, sortDirection))
        .limit(size + 1)
        .fetch();
  }

  @Override
  public long countWithFilter(Long indexInfoId, boolean enabled) {
    return 0;
  }

  // WHERE 절 필터링 조건 메서드

  private BooleanExpression indexInfoIdEq(Long indexInfoId) {
    if (indexInfoId == null) {
      return null;
    }
    return autoSync.indexInfo.id.eq(indexInfoId);
  }

  private BooleanExpression enabledEq(boolean enabled) {
    return autoSync.isEnable.eq(enabled);
  }

  private BooleanExpression idAfterEq(Long idAfter) {
    if (idAfter == null) {
      return null;
    }
    return autoSync.id.gt(idAfter);
  }

  private OrderSpecifier<?> getOrderSpecifier(String sortField, String softDirection) {
    Order order = "DESC".equalsIgnoreCase(softDirection) ? Order.DESC : Order.ASC;

    // sortField 정렬 필드
    if ("indexInfoId".equalsIgnoreCase(sortField)) {
      return new OrderSpecifier<>(order, autoSync.indexInfo.id);
    }

    if ("isEnable".equalsIgnoreCase(sortField)) {
      return new OrderSpecifier<>(order, autoSync.isEnable);
    }
    return new OrderSpecifier<>(order, autoSync.id);
  }

}
