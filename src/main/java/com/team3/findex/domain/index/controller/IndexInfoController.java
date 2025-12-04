package com.team3.findex.domain.index.controller;

import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDtoSummaryDto;
import com.team3.findex.domain.index.service.IndexInfoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-infos")
public class IndexInfoController {

  private final IndexInfoService indexInfoService;

  @PostMapping
  public ResponseEntity<IndexInfoDto> create(@RequestBody @Valid IndexInfoCreateRequest request) {
    IndexInfoDto dto = indexInfoService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexInfoDto> update(
      @PathVariable Long id,
      @RequestBody @Valid IndexInfoUpdateRequest request
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

}