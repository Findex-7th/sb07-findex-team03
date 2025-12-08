package com.team3.findex.domain.index.swaggerDocs;

import com.team3.findex.common.exception.dto.ErrorResponse;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoSummaryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "지수 정보 API", description = "지수 정보 CRUD 및 조회 API")
public interface IndexInfoDocs {

  // ----------------------------------------------------
  // 1) 생성
  // ----------------------------------------------------
  @Operation(summary = "지수 정보 생성", description = "새로운 지수 정보를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "생성 성공",
          content = @Content(schema = @Schema(implementation = IndexInfoDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<IndexInfoDto> create(IndexInfoCreateRequest request);


  // ----------------------------------------------------
  // 2) 수정
  // ----------------------------------------------------
  @Operation(summary = "지수 정보 수정", description = "기존 지수 정보를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = IndexInfoDto.class))),
      @ApiResponse(responseCode = "404", description = "대상 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<IndexInfoDto> update(Long id, IndexInfoUpdateRequest request);


  // ----------------------------------------------------
  // 3) 삭제
  // ----------------------------------------------------
  @Operation(summary = "지수 정보 삭제", description = "ID로 지수 정보를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "대상 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Void> delete(Long id);


  // ----------------------------------------------------
  // 4) 전체 조회
  // ----------------------------------------------------
  @Operation(summary = "전체 지수 정보 조회", description = "정렬 기준을 포함하여 전체 지수 정보를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = IndexInfoDto.class)))
  })
  ResponseEntity<List<IndexInfoDto>> getAllIndexInfos(String sort, String order);


  // ----------------------------------------------------
  // 5) 커서 기반 검색
  // ----------------------------------------------------
  @Operation(summary = "지수 정보 커서 조회", description = "필터링, 정렬, 커서 기반 페이지네이션을 사용한 지수 정보 목록 조회입니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseIndexInfoDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<CursorPageResponseIndexInfoDto> searchIndexInfos(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      String order,
      Integer size
  );


  // ----------------------------------------------------
  // 6) 요약 정보 조회
  // ----------------------------------------------------
  @Operation(summary = "지수 요약 목록 조회", description = "지수 ID·분류·이름만 포함한 간단한 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = IndexInfoSummaryDto.class)))
  })
  ResponseEntity<List<IndexInfoSummaryDto>> getSummaries(String sort, String order);
}
