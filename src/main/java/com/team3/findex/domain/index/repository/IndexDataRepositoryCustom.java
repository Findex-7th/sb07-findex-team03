package com.team3.findex.domain.index.repository;

import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.IndexDataFindCondition;
import com.team3.findex.domain.index.dto.IndexDataFindSort;
import com.team3.findex.domain.index.dto.IndexInfoFindCondition;
import com.team3.findex.domain.index.dto.IndexInfoFindSort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexDataRepositoryCustom {
    List<IndexData> findByCondition(Long cursor,
                                    IndexDataFindCondition condition,
                                    int size,
                                    IndexDataFindSort sort);
}
