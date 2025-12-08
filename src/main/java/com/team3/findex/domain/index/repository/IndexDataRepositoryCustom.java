package com.team3.findex.domain.index.repository;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexDataRepositoryCustom {
    List<IndexData> findByCondition(Long idAfter,
                                    IndexDataFindCondition condition,
                                    int size,
                                    IndexDataFindSort sort);

    Long CountByCondition(IndexDataFindCondition condition);
}
