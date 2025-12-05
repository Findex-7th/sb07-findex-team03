package com.team3.findex.controller;

import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataExcelDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.PeriodType;
import com.team3.findex.service.Interface.IndexDataServiceInterface;
import com.team3.findex.swaggerDocs.IndexDataDoc;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final IndexDataServiceInterface indexDataService;

    /**
     * 지수 데이터 목록 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<CursorPageResponse<IndexDataDto>> getAllIndexData(
//        @Valid @RequestParam IndexDataListRequest request){ //??
        @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,
        @RequestParam(value = "startDate",   required = false) LocalDate startDate,
        @RequestParam(value = "endDate",     required = false) LocalDate endDate,
        @RequestParam(value = "idAfter",     required = false) Long idAfter,
        @RequestParam(value = "cursor",      required = false) String cursor,
        @Valid @RequestParam(value = "sortField")              String sortField,
        @Valid @RequestParam(value = "sortDirection")          String sortDirection,
        @Valid @RequestParam(value = "size")                   Integer size
    ){

        CursorPageResponse<IndexDataDto> responseDto  = indexDataService.getAllIndexData(sortField, sortDirection, size);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(responseDto);
    }

    /**
     * 지수 데이터 등록
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
     * 지수 데이터 삭제
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
     * 지수 데이터 수정
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
     * 지수 차트 조회
     * @return
     */
    @GetMapping("/{id}/chart")
    public ResponseEntity<IndexChartDto> getChartData(
        @Valid @PathVariable(value = "id") Long id,
        @RequestParam(value = "periodType", required = false) PeriodType periodType
    ){

        // 대시보드
        IndexChartDto indexChartDto = indexDataService.getChartData(id, periodType);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexChartDto);
    }

    /**
     * 지수 성과 랭킹 조회
     * http://api/index-data/performance/rank?indexInfoId=123&periodType=WEEKLY&limit=10'
     * 200
     * Response body = []
     * Response headers = [
     *     connection: keep-alive
     *     content-type: application/json
     *     date: Mon,01 Dec 2025 09:18:14 GMT
     *     server: nginx/1.27.5
     *     transfer-encoding: chunked
     *  ]
     * @return
     */
    @GetMapping("/performance/rank")
    public ResponseEntity<List<RankedIndexPerformanceDto>> performanceRank(
        @RequestParam(value = "indexInfoId", required = false) long indexInfoId,
        @RequestParam("periodType") PeriodType periodType,
        @RequestParam("limit") int limit
    ){

        List<RankedIndexPerformanceDto> rankedDtoList = indexDataService.performanceRank(indexInfoId, periodType, limit);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(rankedDtoList);
    }

    /**
     * 관심 지수 성과 조회
     * @return
     */
    @GetMapping("/performance/favorite")
    public ResponseEntity<List<IndexPerformanceDto>> performanceFavorite(
        @RequestParam("periodType") PeriodType periodType
    ){

        List<IndexPerformanceDto> indexPerformanceDtoList = indexDataService.performanceFavorite(
            periodType);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(indexPerformanceDtoList);
    }



    /**
     * 지수 데이터 CSV export
     * @return
     */
    @GetMapping("/export/csv")
    public void exportCsv(
//        @Valid @RequestParam("") ExportCsvRequest request
        @RequestParam(value = "indexInfoId")                    Long indexInfoId,
        @RequestParam(value = "startDate", required = false)    String startDate,
        @RequestParam(value = "endDate",   required = false)    String endDate,
        @RequestParam(value = "sortField", required = false)    String sortField,
        @RequestParam(value = "sortDirection")                  String sortDirection,
        HttpServletResponse response) throws IOException {

      List<IndexDataExcelDto> csvDtos = indexDataService.exportCsv(indexInfoId,
          startDate, endDate, sortField, sortDirection);

      response.setContentType("text/csv; charset=UTF-8");
      response.setHeader("Content-Disposition",
          "attachment; filename=index-data-export-" + LocalDate.now() + ".csv");

      PrintWriter writer = response.getWriter();
      writer.println("기준일자,시가,종가,고가,저가,전일,대비 등락폭,등락률,거래량,거래대금,상장시가총액");

      csvDtos.forEach(csvDto -> writer.println(csvDto.toString()));

      writer.flush();
    }
}
