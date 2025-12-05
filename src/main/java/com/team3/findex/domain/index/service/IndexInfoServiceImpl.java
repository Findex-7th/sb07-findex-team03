package com.team3.findex.domain.index.service;

import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.mapper.IndexInfoMapper;
import com.team3.findex.repository.IndexInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class IndexInfoServiceImpl implements IndexInfoService {

    private final IndexInfoRepository indexInfoRepository;
    private final IndexInfoMapper indexInfoMapper;

    @Transactional
    @Override
    public IndexInfoDto create(IndexInfoCreateRequest request) {
        return indexInfoMapper.toDto(
                indexInfoRepository.save(new IndexInfo(
                        request.indexClassification(),
                        request.indexName(),
                        request.employedItemsCount(),
                        request.basePointInTime(),
                        request.baseIndex(),
                        request.favorite(),
                        request.sourceType()
                ))
        );
    }

    @Transactional
    @Override
    public IndexInfoDto update(IndexInfoUpdateRequest request) {
        IndexInfo indexInfo = indexInfoRepository.findById(request.id())
                .orElseThrow(() -> new CustomException(ErrorCode.INDEX_INFO_NOT_FOUND));
        indexInfo.update(
                request.employedItemsCount(),
                request.basePointInTime(),
                request.baseIndex(),
                request.favorite()
        );

        return indexInfoMapper.toDto(indexInfo);
    }

    @Override
    public IndexInfoDto getById(Long id) {
        return indexInfoMapper.toDto(
                indexInfoRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.INDEX_INFO_NOT_FOUND))
        );
    }

    @Override
    public List<IndexInfoDto> findAll() {
        return indexInfoRepository.findAll().stream()
                .map(indexInfoMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        indexInfoRepository.deleteById(id);
    }




}
