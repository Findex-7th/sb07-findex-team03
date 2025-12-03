package com.team3.findex.common.openapi.mapper;

import com.team3.findex.common.openapi.dto.IndexApiResponse;
import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import com.team3.findex.common.openapi.dto.IndexSyncData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenApiMapper {

    public IndexInfoSyncData toIndexInfoSyncData(IndexApiResponse indexApiResponse) {
        return IndexInfoSyncDataMapper.toIndexInfoSyncData(indexApiResponse);
    }


    public List<IndexInfoSyncData> toIndexInfoSyncData(List<IndexApiResponse> indexApiResponse) {
        return IndexInfoSyncDataMapper.toIndexInfoSyncData(indexApiResponse);
    }

    public IndexSyncData toIndexSyncData(IndexApiResponse indexApiResponse) {
        return IndexSyncMapper.toIndexSyncData(indexApiResponse);
    }

    public List<IndexSyncData> toIndexSyncData(List<IndexApiResponse> indexApiResponse) {
        return IndexSyncMapper.toIndexSyncData(indexApiResponse);
    }


}
