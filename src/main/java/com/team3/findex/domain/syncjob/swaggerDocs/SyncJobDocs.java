package com.team3.findex.domain.syncjob.swaggerDocs;

import com.team3.findex.common.exception.dto.ErrorResponse;
import com.team3.findex.domain.syncjob.dto.request.CursorPageRequestSyncJobDto;
import com.team3.findex.domain.syncjob.dto.request.IndexDataSyncRequest;
import com.team3.findex.domain.syncjob.dto.response.CursorPageResponseSyncJobDto;
import com.team3.findex.domain.syncjob.dto.response.SyncJobDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "연동 작업 API", description = "지수 정보 및 지수 데이터 연동 작업 API")
public interface SyncJobDocs {

  // ----------------------------------------------------
  // 1) 지수 정보 연동
  // ----------------------------------------------------
  @Operation(
      summary = "지수 정보 연동",
      description = "Open API를 통해 지수 정보를 연동합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "연동 작업 생성 성공",
          content = @Content(schema = @Schema(implementation = SyncJobDto.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<List<SyncJobDto>> getIndexInfo(HttpServletRequest request);


  // ----------------------------------------------------
  // 2) 지수 데이터 연동
  // ----------------------------------------------------
  @Operation(
      summary = "지수 데이터 연동",
      description = "Open API를 통해 지수 데이터를 연동합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "연동 작업 생성 성공",
          content = @Content(schema = @Schema(implementation = SyncJobDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<List<SyncJobDto>> getIndexData(
      IndexDataSyncRequest indexDataSyncRequest,
      HttpServletRequest request
  );


  // ----------------------------------------------------
  // 3) 커서 기반 연동 작업 조회
  // ----------------------------------------------------
  @Operation(
      summary = "연동 작업 목록 조회",
      description = "필터링, 정렬, 커서 기반 페이지네이션을 지원하는 연동 작업 목록 조회 API입니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseSyncJobDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<CursorPageResponseSyncJobDto> cursorPageResponse(
      CursorPageRequestSyncJobDto cursorPageRequestSyncJobDto
  );

}
