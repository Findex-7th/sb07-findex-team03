package com.team3.findex.domain.index.controller;

import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.common.util.CursorEncodingUtil;
import com.team3.findex.domain.index.dto.request.IndexDataCursorRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexDataDto;
import com.team3.findex.domain.index.enums.IndexDataSortField;
import com.team3.findex.domain.index.dto.IndexChartDto;
import com.team3.findex.domain.index.dto.request.IndexDataCreateRequest;
import com.team3.findex.domain.index.dto.IndexDataDto;
import com.team3.findex.domain.index.dto.request.IndexDataUpdateRequest;
import com.team3.findex.domain.index.dto.IndexDataWithInfoDto;
import com.team3.findex.domain.index.dto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.enums.PeriodType;
import com.team3.findex.domain.index.service.IndexDataService;
import com.team3.findex.domain.index.swaggerDocs.IndexDataDoc;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController implements IndexDataDoc {
    private final IndexDataService indexDataService;

    /**
         * 주어진 필터링 및 페이지네이션 매개변수를 기반으로 지수 데이터의 페이지를 조회합니다.
         *
         * @param indexInfoId 필터링할 지수 정보의 ID (선택)
         * @param startDate 날짜 필터의 시작일 (선택)
         * @param endDate 날짜 필터의 종료일 (선택)
         * @param idAfter 페이지네이션을 시작할 지수 데이터의 ID (선택)
         * @param cursor 페이지네이션 상태를 디코딩하기 위한 커서 문자열 (선택)
         * @param sortField 지수 데이터를 정렬할 필드 (선택)
         * @param order 정렬 순서, 오름차순 또는 내림차순 (기본: 내림차순)
         * @param size 응답에 포함될 최대 항목 수 (기본: 10)
         * @return 요청된 지수 데이터를 포함한 {@link CursorPageResponseIndexDataDto}를 담은 {@link ResponseEntity}
         *         또는 필터와 일치하는 데이터가 없는 경우 빈 결과
         */
    @GetMapping
    public ResponseEntity<CursorPageResponseIndexDataDto> getIndexDatas(
            @RequestParam(required = false) Long indexInfoId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(indexDataService.getAllIndexData(
                new IndexDataCursorRequest(
                        indexInfoId,
                        startDate,
                        endDate,
                        idAfter,
                        CursorEncodingUtil.decodeId(cursor),
                        IndexDataSortField.fromString(sortField),
                        Sort.Direction.fromString(order.toUpperCase()),
                        size
                ))
        );
    }

    /**
     * 지수 데이터 등록 ⭕️🎉
     * @return
     */
    @PostMapping
    public ResponseEntity<IndexDataDto> createIndexData(
        @Valid @RequestBody IndexDataCreateRequest request){

        IndexDataDto indexData = indexDataService.createIndexData(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexData);
    }

    /**
     * 지수 데이터 삭제 ⭕️🎉
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteIndexData(
        @Valid @PathVariable("id") Long id
    ){

        indexDataService.deleteIndexData(id);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    /**
     * 지수 데이터 수정 ⭕️🎉
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<IndexDataDto> updateIndexData(
        @Valid @PathVariable("id") Long id,
        @Valid @RequestBody IndexDataUpdateRequest request
    ){

        IndexDataDto indexDataDto = indexDataService.updateIndexData(id, request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexDataDto);
    }

    /**
     **지수 성과 분석 랭킹 ⭕️🎉**
     * 전일/전주/전월 대비 성과 랭킹
     * 성과는 **{종가}**를 기준으로 비교합니다.
     * 🧊🧊🧊지수 성과 🧊🧊🧊🧊
     * @return
     */
    @GetMapping("/performance/rank")
    public ResponseEntity<List<RankedIndexPerformanceDto>> performanceRank(
        @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,
        @RequestParam("periodType") PeriodType periodType,
        @RequestParam("limit") int limit
    ){
        log.info("🧊🧊🧊지수 성과 분석 랭킹");
        List<RankedIndexPerformanceDto> rankedDtoList = indexDataService.performanceRank(indexInfoId, periodType, limit);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(rankedDtoList);
    }

    /**
     * 지수 차트 조회
     * @return
     */
    @GetMapping("/{id}/chart")
    public ResponseEntity<IndexChartDto> getChartData(
        @Valid @PathVariable(value = "id") Long id,
        @RequestParam(value = "periodType", required = false, defaultValue = "YEARLY") ChartPeriodType periodType
    ){
        IndexChartDto indexChartDto = indexDataService.getChartData(id,  periodType);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexChartDto);
    }


    /**
     * 🐠🐠🐠주요 지수 ⭕️🎉
     * 관심 지수 성과 조회
     * @return
     */
    @GetMapping("/performance/favorite")
    public ResponseEntity<List<IndexDataWithInfoDto>> favoriteIndex(
        @RequestParam("periodType") PeriodType periodType
    ){
        List<IndexDataWithInfoDto> indexDataWithInfoDtoList = indexDataService.favoriteIndex(
            periodType);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexDataWithInfoDtoList);
    }


    /**
     * 지수 데이터 CSV export ⭕️🎉
     * @return
     */
    @GetMapping("/export/csv")
    public void exportCsv(
        @RequestParam(value = "indexInfoId")                    Long indexInfoId,
        @RequestParam(value = "startDate", required = false)    String startDate,
        @RequestParam(value = "endDate",   required = false)    String endDate,
        @RequestParam(value = "sortField", required = false)    String sortField,
        @RequestParam(value = "sortDirection")                  String sortDirection,
        HttpServletResponse response) throws IOException {

        indexDataService.exportCsv(indexInfoId, startDate, endDate, sortField, sortDirection, response);
    }
}
