package com.team3.findex.domain.index.controller;

import com.team3.findex.common.util.CursorEncodingUtil;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoCursorRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;
import com.team3.findex.domain.index.enums.SortField;
import com.team3.findex.domain.index.service.IndexInfoService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-infos")
public class IndexInfoController {

    private final IndexInfoService indexInfoService;

    @PostMapping
    public ResponseEntity<IndexInfoDto> create(@RequestBody IndexInfoCreateRequest request) {
        IndexInfoDto dto = indexInfoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfoDto> update(
            @PathVariable Long id,
            @RequestBody IndexInfoUpdateRequest request
    ) {
        IndexInfoDto dto = indexInfoService.update(
                new IndexInfoUpdateRequest(
                        id,
                        request.employedItemsCount(),
                        request.basePointInTime(),
                        request.baseIndex(),
                        request.favorite()
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        indexInfoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<IndexInfoDto>> getAllIndexInfos(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(indexInfoService.findAllSorted(sort, order));
    }

    //   페이지네이션
    @GetMapping
    public ResponseEntity<CursorPageResponseIndexInfoDto> searchIndexInfos(
            @RequestParam(required = false) String classification,
            @RequestParam(required = false) String indexName,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        CursorPageResponseIndexInfoDto response =
                indexInfoService.searchIndexInfos(
                        new IndexInfoCursorRequest(
                                classification,
                                indexName,
                                favorite,
                                idAfter,
                                CursorEncodingUtil.decodeId(cursor),
                                SortField.fromString(sortField),
                                Sort.Direction.fromString(order),
                                size
                        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<IndexInfoSummaryDto>> getSummaries(
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(indexInfoService.getSummaryList(sort, order));
    }


}