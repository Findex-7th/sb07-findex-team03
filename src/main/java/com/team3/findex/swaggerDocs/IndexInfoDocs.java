package com.team3.findex.swaggerDocs;

import java.util.List;
import com.team3.findex.common.exception.dto.ErrorResponse;
import com.team3.findex.domain.index.dto.request.IndexInfoCreateRequest;
import com.team3.findex.domain.index.dto.request.IndexInfoUpdateRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDto;
import com.team3.findex.domain.index.dto.response.IndexInfoDtoSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "지수 정보 API", description = "지수 정보 관리 API")
public interface IndexInfoDocs {

  // 지수 정보 목록 조회
  @Operation(summary = "지수 정보 목록 조회", description = "지수 정보 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 목록 조회 성공",
          content = @Content(
              schema = @Schema(implementation = CursorPageResponseIndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 필터 값 등)",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<CursorPageResponseIndexInfoDto> getIndexInfoList();


  // 지수 정보 등록
  @Operation(summary = "지수 정보 등록", description = "새로운 지수 정보를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "201",
          description = "지수 정보 생성 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 필터 값 등)",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody IndexInfoCreateRequest request);


  // 지수 정보 조회
  @Operation(summary = "지수 정보 조회", description = "ID로 지수 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 조회 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "조회할 지수 정보를 찾을 수 없음",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<IndexInfoDto> getIndexInfo(Long id);

  // 지수 정보 삭제
  @Operation(summary = "지수 정보 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "지수 정보 삭제 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "삭제할 지수 정보를 찾을 수 없음",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<Void> deleteIndexInfo(Long id);


  // 지수 정보 수정
  @Operation(summary = "지수 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 조회 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "조회할 지수 정보를 찾을 수 없음",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<IndexInfoDto> updateIndexInfo(Long id,
      @RequestBody IndexInfoUpdateRequest request);


  // 지수 정보 요약 목록 조회
  @Operation(summary = "지수 요약 목록 조회", description = "지수 ID, 분유 ,이름만 포함한 전체 지수 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 요약 목록 조회 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDtoSummaryDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<List<IndexInfoDtoSummaryDto>> getIndexInfoSummaries();
}




