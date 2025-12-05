package com.team3.findex.domain.index.controller;

import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;
import com.team3.findex.domain.index.service.IndexInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  // 페이지네이션
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> search(
      @RequestParam(required = false) String classification,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean favorite,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) Long cursorId,
      @RequestParam(defaultValue = "20") int size
  ) {
    CursorPageResponseIndexInfoDto response =
        indexInfoService.search(classification, name, favorite, sort, cursorId, size);

    return ResponseEntity.ok(response);
  }
  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> getSummaries() {
    return ResponseEntity.ok(indexInfoService.getSummaryList());
  }


}