package com.team3.findex.domain.index.repository;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.IndexInfoFindCondition;
import com.team3.findex.domain.index.dto.IndexInfoFindSort;

import java.util.List;

/**
 * IndexInfo Custom Repository
 * <p>
 * QueryDSL을 사용한 동적 쿼리를 제공합니다.
 * </p>
 */
public interface IndexInfoRepositoryCustom {

    List<IndexInfo> findWithCursor(Long cursor, int size, IndexInfoFindSort sort);

    List<IndexInfo> findByCondition(Long cursor,
                                    IndexInfoFindCondition condition,
                                    int size,
                                    IndexInfoFindSort sort);
}
