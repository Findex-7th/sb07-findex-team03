package com.team3.findex.domain.index.service.impl;

import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
import com.team3.findex.common.util.ReflectionUtil;
import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.enums.PeriodType;
import com.team3.findex.domain.index.dto.ChartDataPointDto;
import com.team3.findex.domain.index.dto.IndexDataFindCondition;
import com.team3.findex.domain.index.dto.IndexDataFindSort;
import com.team3.findex.domain.index.dto.request.IndexDataCursorRequest;
import com.team3.findex.domain.index.dto.response.CursorPageResponseIndexDataDto;
import com.team3.findex.domain.index.dto.IndexDataWithInfoDto;
import com.team3.findex.domain.index.dto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.dto.IndexChartDto;
import com.team3.findex.domain.index.dto.request.IndexDataCreateRequest;
import com.team3.findex.domain.index.dto.IndexDataDto;
import com.team3.findex.domain.index.dto.request.IndexDataUpdateRequest;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.mapper.IndexChartMapper;
import com.team3.findex.domain.index.mapper.IndexDataMapper;
import com.team3.findex.domain.index.repository.IndexDataRepository;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import com.team3.findex.domain.index.service.IndexDataService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import static com.team3.findex.common.util.CursorEncodingUtil.encodeId;


@Slf4j
@Service
@RequiredArgsConstructor
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class IndexDataServiceImpl extends HttpServlet implements IndexDataService {

    private final IndexDataRepository indexDataRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final IndexDataMapper indexDataMapper;
    private final IndexChartMapper indexChartMapper;


    private LocalDate getChartPeriodTypeDate(ChartPeriodType periodType) {
        LocalDate fromData = LocalDate.now();

        switch (periodType) {
            case MONTHLY -> fromData = fromData.minusMonths(1);
            case QUARTERLY -> fromData = fromData.minusMonths(3);
            case YEARLY -> fromData = fromData.minusYears(1);
            default -> throw new IllegalArgumentException("🚨getPeriodTypeDate.periodType error! ");
        }

        return fromData;
    }

    private LocalDate getPeriodTypeDate(PeriodType periodType) {
        LocalDate fromData = LocalDate.now();

        switch (periodType) {
            case DAILY -> fromData = fromData.minusDays(1);
            case WEEKLY -> fromData = fromData.minusWeeks(1);
            case MONTHLY -> fromData = fromData.minusMonths(1);
            default -> throw new IllegalArgumentException("🚨getPeriodTypeDate.periodType error! ");
        }

        return fromData;
    }

    @Transactional
    @Override
    public IndexDataDto createIndexData(IndexDataCreateRequest request) {

        IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId())
                .orElseThrow(() -> new CustomException(ErrorCode.INDEX_INFO_NOT_FOUND));

        IndexData indexData = IndexData.from(indexInfo, request);
        IndexData saveIndexData = indexDataRepository.save(indexData);
        return indexDataMapper.toDTO(saveIndexData);
    }


    @Transactional
    @Override
    public void deleteIndexData(Long id) {

        indexDataRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("🚨 error - deleteIndexData.id"));

        indexDataRepository.deleteById(id);
    }

    @Transactional
    @Override
    public IndexDataDto updateIndexData(Long id, IndexDataUpdateRequest request) {

        IndexData indexData = indexDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("🚨 error - updateIndexData.id"));

        indexData.setUpdateIndexData(request);

        return indexDataMapper.toDTO(indexData);
    }

    //🐠🐠🐠주요 지수🐠🐠🐠
    @Override
    public List<IndexDataWithInfoDto> favoriteIndex(PeriodType periodType) {
        // {종가}를 기준으로 비교
        LocalDate end = LocalDate.from(LocalDateTime.now());
        LocalDate start = getPeriodTypeDate(periodType);

        List<IndexDataWithInfoDto> dooList = indexDataRepository.findAllFavoriteIndex(start, end)
                .stream()
                .map(dto -> {
                    double value = dto.closingPriceAvg().doubleValue() - dto.versusAvg().doubleValue();
                    return IndexDataWithInfoDto.fromBeforeDto(dto, value);
                })
                .toList();

        log.info("🚨 favoriteIndex = " + String.valueOf(dooList.size()));
        return dooList;
    }


    @Override
    public void exportCsv(Long indexInfoId,
                          String startDate,
                          String endDate,
                          String sortField,
                          String sortDirection,
                          HttpServletResponse response) throws IOException {

        if (startDate == null || startDate.isBlank())
            startDate = "1970-01-01";

        if (endDate == null || endDate.isBlank())
            endDate = String.valueOf(LocalDate.now());

        if (sortField == null || sortField.isBlank()) {
            sortField = "baseDate";
        }

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        Sort.Order order =
                (0 != sortDirection.compareTo("desc")) ? Order.desc(sortField) : Order.asc(sortField);

        List<IndexData> indexDataList = indexDataRepository.findAllExportCsvData(indexInfoId,
                startLocalDate,
                endLocalDate,
                Sort.by(order));

        if (indexDataList.isEmpty())
            throw new NoSuchElementException("해당하는 CSV 자료 없음");

        //Content-type : text/html;charset=utf-8
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=index-data-export-" + LocalDate.now() + ".csv");
        response.setStatus(HttpServletResponse.SC_OK); // 200 OK

        PrintWriter writer = response.getWriter();
        writer.println("index-data-export-" + LocalDate.now().toString());
        writer.println("기준일자, 시가, 종가, 고가, 저가, 전일 대비 등락폭, 등락률, 거래량, 거래대금, 상장시가총액");

        indexDataList.stream()
                .map(indexDataMapper::toExcelDto)
                .forEach(excelDto -> {
                    String line = ReflectionUtil.dtoToValueList(excelDto).stream()
                            .map(String::valueOf)  // Object → Strin
                            .collect(Collectors.joining(",")); // 쉼표로 연결
                    writer.println(line);
                });
        writer.flush();
    }

    /**
     * 커서 기반 페이지네이션으로 IndexData 목록을 조회합니다.
     * <p>
     * 지수 선택, 날짜 범위, 정렬 조건을 기반으로 데이터를 필터링하고 정렬하여 반환합니다.
     * </p>
     *
     * @param request 커서, 지수 ID, 날짜 범위(시작/종료), 페이지 크기, 정렬 필드 및 방향을 포함하는 요청 객체
     * @return 페이지네이션된 IndexData 목록, 다음 커서, 총 개수, 다음 페이지 존재 여부를 포함하는 응답 DTO
     */
    @Override
    public CursorPageResponseIndexDataDto getAllIndexData(IndexDataCursorRequest request) {
        int pageSize = request.size() == null ? 10 : request.size();

        IndexDataFindCondition condition = new IndexDataFindCondition(
                request.indexInfoId(),
                request.startDate(),
                request.endDate()
        );

        List<IndexData> results = indexDataRepository.findByCondition(
                request.idAfter(),
                condition,
                pageSize,
                new IndexDataFindSort(
                        request.sortField(),
                        request.order()
                )
        );

        boolean hasNext = results.size() > pageSize;
        List<IndexData> pageContent = hasNext ? results.subList(0, pageSize) : results;

        String nextCursor = hasNext ? encodeId(results.get(results.size() - 1).getId()) : null;
        Long nextIdAfter = pageContent.isEmpty() ? null
                : pageContent.get(pageContent.size() - 1).getId();

        return new CursorPageResponseIndexDataDto(
                indexDataMapper.toDtoList(pageContent),
                nextCursor,
                nextIdAfter,
                pageContent.size(),
                request.idAfter() == null ?
                        indexDataRepository.CountByCondition(condition) : null,
                hasNext
        );
    }
}
