package com.team3.findex.swaggerDocs;

import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.ExportCsvRequest;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.ChartPeriodType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "지수 데이터 API", description = "지수 데이터 관리 API")
public interface IndexDataDoc {

    @Operation(summary = "지수 데이터 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "200",
            description = "지수 데이터 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "\"잘못된 요청 (유효하지 않은 필터 값 등)\"",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<CursorPageResponse<IndexDataDto>> getAllIndexData(
//        @Valid @RequestParam IndexDataListRequest request){ //??
        @Valid @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,
        @Valid @RequestParam(value = "startDate",   required = false) LocalDate startDate,
        @Valid @RequestParam(value = "endDate",     required = false) LocalDate endDate,
        @Valid @RequestParam(value = "idAfter",     required = false) Long idAfter,
        @Valid @RequestParam(value = "cursor",      required = false) String cursor,
        @Valid @RequestParam(value = "sortField")                     String sortField,
        @Valid @RequestParam(value = "sortDirection")                 String sortDirection,
        @Valid @RequestParam(value = "size")                          Integer size
    );


    @Operation(summary = "지수 데이터 등록")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 데이터 값 등)",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "201",
            description = "지수 데이터 생성 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "참조하는 지수 정보를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<IndexDataDto> createIndexData(
        @Valid @RequestBody IndexDataCreateRequest request);


    @Operation(summary = "지수 정보 삭제")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "지수 정보 삭제 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<Object> deleteIndexData(
        @Valid @PathVariable("id") Long id
    );


    @Operation(summary = "지수 정보 수정")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "수정할 지수 정보를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "200",
            description = "지수 정보 수정 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 필드 값 등)",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<IndexDataDto> updateIndexData(
        @Valid @PathVariable("id") Long id,
        @Valid @RequestBody IndexDataUpdateRequest request
    );


    @Operation(summary = "지수 차트 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "차트 데이터 조회 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 기간 유형 등)",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "지수 정보를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<IndexChartDto> getChartData(
        @Valid @PathVariable(value = "id") Long id,
        @RequestParam(value = "periodType", required = false) ChartPeriodType chartPeriodType
    );


    @Operation(summary = "지수 성과 랭킹 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 기간 유형 등)",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "200",
            description =  "성과 랭킹 조회 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    public ResponseEntity<List<RankedIndexPerformanceDto>> performanceRank(
        @RequestParam("indexInfoId") long indexInfoId,
        @RequestParam("periodType") String periodType,
        @RequestParam("limit") int limit
    );


    @Operation(summary = "관심 지수 성과 조회")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "200",
            description =  "관심 지수 성과 조회 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<List<IndexPerformanceDto>> performanceFavorite(
        @RequestParam("periodType") ChartPeriodType chartPeriodType
    );


    @Operation(summary = "지수 데이터 CSV export")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description =  "CSV 파일 생성 성공",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 필터 값 등)",
            content = @Content(
                schema = @Schema(implementation = IndexDataDoc.class)
            )
        )
    })
    ResponseEntity<Object> exportCsv(
//        @Valid @RequestParam("") ExportCsvRequest request
        @RequestParam(value = "indexInfoId")                    Long indexInfoId,
        @RequestParam(value = "startDate", required = false)    String startDate,
        @RequestParam(value = "endDate",   required = false)    String endDate,
        @RequestParam(value = "sortField", required = false)    String sortField,
        @RequestParam(value = "sortDirection")                  String sortDirection);
}
