package com.team3.findex.domain.index.service;

import com.team3.findex.common.openapi.OpenApiProvider;
import com.team3.findex.domain.autosync.service.AutoSyncService;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.domain.index.dto.IndexInfoFindCondition;
import com.team3.findex.domain.index.dto.IndexInfoFindSort;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoCursorRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;
import com.team3.findex.domain.index.mapper.IndexInfoMapper;
import com.team3.findex.repository.AutoSyncRepository;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.team3.findex.common.util.CursorEncodingUtil.encodeId;


@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {

    private final IndexInfoRepository indexInfoRepository;
    private final IndexInfoMapper indexInfoMapper;
    private final IndexDataRepository indexDataRepository;
    private final AutoSyncService autoSyncService;
    private final OpenApiProvider openApiProvider;
    private final AutoSyncRepository autoSyncRepository;

    @Override
    public IndexInfoDto create(IndexInfoCreateRequest request) {
        boolean exists = indexInfoRepository.existsByIndexClassificationAndIndexName(
                request.indexClassification(),
                request.indexName()
        );

        if (exists) {
            throw new IllegalArgumentException("해당 지수 분류명과 지수명 조합은 이미 존재합니다.");
        }

        IndexInfo indexInfo = new IndexInfo(
                request.indexClassification(),
                request.indexName(),
                request.employedItemsCount(),
                LocalDate.parse(request.basePointInTime()),
                request.baseIndex(),
                request.favorite() != null && request.favorite(),
                SourceType.USER
        );

        IndexInfo saved = indexInfoRepository.save(indexInfo);

        // 자동 연동 설정 저장
        autoSyncService.create(indexInfo);
        return indexInfoMapper.toDto(saved);
    }

    @Override
    public IndexInfoDto update(IndexInfoUpdateRequest request) {
        IndexInfo indexInfo = indexInfoRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));

        LocalDate parsedDate = null;
        if (request.basePointInTime() != null) {
            parsedDate = LocalDate.parse(request.basePointInTime());
        }

        indexInfo.update(
                request.employedItemsCount(),
                parsedDate,
                request.baseIndex(),
                request.favorite()
        );

        IndexInfo updated = indexInfoRepository.save(indexInfo);
        return indexInfoMapper.toDto(updated);
    }

    @Override
    public IndexInfoDto getById(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));
        return indexInfoMapper.toDto(indexInfo);
    }

    @Override
    public List<IndexInfoDto> findAll() {
        return indexInfoMapper.toDtoList(indexInfoRepository.findAll());

    }

    @Override
    public List<IndexInfoDto> findAllSorted(String sortKey, String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort;

        if (sortKey == null || sortKey.isBlank()) {
            sort = Sort.by(direction, "id");
        } else {
            sort = Sort.by(direction, sortKey);
        }

        List<IndexInfo> result = indexInfoRepository.findAll(sort);
        return result.stream()
                .map(indexInfoMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public void delete(Long id) {
        IndexInfo indexInfo = indexInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지수 정보를 찾을 수 없습니다."));

        indexDataRepository.deleteByIndexInfoId(indexInfo.getId());
        autoSyncRepository.deleteByIndexInfoId(indexInfo.getId());
        indexInfoRepository.delete(indexInfo);
    }

    @Transactional
    public void autoSyncFromOpenApi() {
        // Open Api에서 오늘 지수 정보 리스트 갖고 옴
        var list = openApiProvider.getIndexInfoDataByDate(LocalDate.now());

        list.forEach(sync -> {
            indexInfoRepository
                    .findByIndexClassificationAndIndexName(
                            sync.indexClassification(),
                            sync.indexName()
                    )
                    .ifPresentOrElse(
                            // 이미 존재하면 자동 수정
                            existing -> {
                                existing.update(
                                        sync.employedItemsCount(),
                                        sync.basePointInTime(),
                                        sync.baseIndex(),
                                        existing.getFavorite()
                                );
                                indexInfoRepository.save(existing);
                            },
                            // 존재하지 않으면 자동 등록
                            () -> {
                                IndexInfo info = new IndexInfo(
                                        sync.indexClassification(),
                                        sync.indexName(),
                                        sync.employedItemsCount(),
                                        sync.basePointInTime(),
                                        sync.baseIndex(),
                                        false,
                                        SourceType.OPEN_API
                                );
                                indexInfoRepository.save(info);
                                autoSyncService.create(info);
                            }
                    );
        });
    }

    @Override
    public List<IndexInfoSummaryDto> getSummaryList(String sortKey, String order) {
        Sort.Direction direction = "desc".equals(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort;

        if( sortKey == null ||sortKey.isEmpty()){
            sort = Sort.by(direction, "id");
        } else {
            sort = Sort.by(direction, sortKey);
        }

        List<IndexInfo> list = indexInfoRepository.findAll(sort);
        return indexInfoMapper.toSummaryDtoList(list);
    }

    // =============== 여기부터 ========================
    // TODO 로딩문제 해결하기
    @Override
    public CursorPageResponseIndexInfoDto searchIndexInfos(IndexInfoCursorRequest request) {
        int pageSize = request.size() == null ? 10 : request.size();

        List<IndexInfo> results = indexInfoRepository.findByCondition(
                request.cursor(),
                new IndexInfoFindCondition(
                        request.indexClassification(),
                        request.indexName(),
                        request.favorite()
                ),
                pageSize,
                new IndexInfoFindSort(
                        request.indexInfoSortField(),
                        request.sortDirection()
                )
        );

        boolean hasNext = results.size() > pageSize;
        List<IndexInfo> pageContent = hasNext ? results.subList(0, pageSize) : results;

        String nextCursor = hasNext ? encodeId(results.get(results.size() - 1).getId()) : null;
        String nextIdAfter = pageContent.isEmpty()
                ? null
                : encodeId(pageContent.get(pageContent.size() - 1).getId());

        return new CursorPageResponseIndexInfoDto(
                indexInfoMapper.toDtoList(pageContent),
                nextCursor,
                nextIdAfter,
                pageContent.size(),
                indexInfoRepository.count(),
                hasNext
        );
    }
}
