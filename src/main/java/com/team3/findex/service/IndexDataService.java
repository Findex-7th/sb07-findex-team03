package com.team3.findex.service;

//import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
import com.team3.findex.common.util.ReflectionUtil;
import com.team3.findex.domain.index.ChartPeriodType;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.PeriodType;
import com.team3.findex.dto.indexDataDto.ChartDataPointDto;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.IndexDataWithInfoDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
//import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
//import com.team3.findex.entity.index.IndexPerformance;
//import com.team3.findex.entity.index.RankedIndexPerformance;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.mapper.IndexChartMapper;
import com.team3.findex.domain.index.mapper.IndexDataMapper;
//import com.team3.findex.mapper.IndexPerformanceMapper;
//import com.team3.findex.mapper.RankedIndexPerformanceMapper;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.service.Interface.IndexDataServiceInterface;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
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


@Slf4j
@Service
@RequiredArgsConstructor
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class IndexDataService extends HttpServlet implements IndexDataServiceInterface {

    private final IndexDataRepository indexDataRepository;
    private final IndexInfoRepository indexInfoRepository;

    //    private final RankedIndexPerformanceMapper rankedIndexPerformanceMapper;
//    private final IndexPerformanceMapper indexPerformanceMapper;
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


    @Override
    public CursorPageResponse<IndexDataDto> getAllIndexData(
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate,
        Long idAfter,
        String strCursor,
        String sortField,
        String sortDirection,
        Integer size) {

        if (null == sortField)
            throw new IllegalArgumentException("🚨sortField is null");
        if (null == sortDirection)
            throw new IllegalArgumentException("🚨 sortDirection is null");
        if (null == size)
            throw new IllegalArgumentException("🚨size is null");

//        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(idAfter.intValue(),
//            size,
//            Sort.by(direction, sortField));
//
//        LocalDate cursor = LocalDate.parse(strCursor);
//
//        // 커서 페이지
//        Slice<IndexDataDto> slice = indexDataRepository.findAllIndexDataWithIndexInfo(
//                indexInfoId,
//                startDate,
//                endDate,
//                Optional.ofNullable(cursor).orElse(LocalDate.now().toString()),
//                pageable)
//            .stream()
//            .map(indexDataMapper::toDTO);
//
//        Instant nextCursor = null;
//        if (!slice.getContent().isEmpty()) {
//            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
//                .createdAt();
//        }
//
//        return pageResponseMapper.fromSlice(slice, nextCursor);
        return null;
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


    /**
     * *지수 성과 분석 랭킹** 전일/전주/전월 대비 성과 랭킹 성과는 **{종가}**를 기준으로 비교합니다. 🧊🧊🧊지수 성과 분석 랭킹 🧊🧊🧊🧊
     *
     * @return
     */
    @Override
    public List<RankedIndexPerformanceDto> performanceRank(Long indexInfoId, PeriodType periodType,
        int limit) {

        LocalDate end = LocalDate.from(LocalDateTime.now());
        LocalDate start = getPeriodTypeDate(periodType);

        List<IndexDataWithInfoDto> indexDataWithInfoDtoList = indexDataRepository
            .findAllPerformanceRank(indexInfoId, start, end, PageRequest.of(0, limit))
            .stream()
            .map(dto ->  {
                double value = dto.currentPrice().doubleValue() - dto.versus().doubleValue();
                return IndexDataWithInfoDto.fixCurrentPriceDto(dto, value);
            })
            .toList();

        log.info("🚨🚨performanceRank = " + String.valueOf(indexDataWithInfoDtoList.size()));

        List<RankedIndexPerformanceDto> rankedDto = new ArrayList<>();

        for (int i = 0; i < indexDataWithInfoDtoList.size(); i++) {

            rankedDto.add(new RankedIndexPerformanceDto(indexDataWithInfoDtoList.get(i), i + 1));
        }

        return rankedDto;
    }


    @Transactional
    @Override
    public IndexChartDto getChartData(Long id, ChartPeriodType periodType) {

        IndexInfo indexInfo = indexInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("🚨indexInfo.id error!"));

        LocalDate now = LocalDate.from(LocalDateTime.now());
        LocalDate from = getChartPeriodTypeDate(periodType);
//        List<ChartDataPointDto> data = indexDataRepository.findChartData(id, from, now);
        List<ChartDataPointDto> ma5 = indexDataRepository.findMa5(id, from, now);
        log.info("🐳 ma5 = " + String.valueOf(ma5.size()));

        List<ChartDataPointDto> ma20 = indexDataRepository.findMa20(id, from, now);
        log.info("🐳 ma20 = " + String.valueOf(ma20.size()));

        List<ChartDataPointDto> pointDtoList = new ArrayList<>();
        switch (periodType) {
            case MONTHLY  -> pointDtoList = indexDataRepository.findMonthlySeries(id, from, now);
            case QUARTERLY -> pointDtoList = indexDataRepository.findQuarterlySeries(id, from, now);
            case YEARLY -> pointDtoList = indexDataRepository.findYearlySeries(id, from, now);
            default -> throw new IllegalArgumentException("🚨getPeriodTypeDate.periodType error! ");
        }

        log.info("🐳 pointDtoList = " + String.valueOf(pointDtoList.size()));

        IndexChartDto indexChartDto = new IndexChartDto(
            indexInfo.getId(),
            indexInfo.getIndexClassification(),
            indexInfo.getIndexName(),
            periodType,
            pointDtoList,
            ma5,
            ma20
        );

        log.info("🐳 pointDtoList = " + indexChartDto.toString());

        return indexChartDto;
    }


    //🐠🐠🐠주요 지수🐠🐠🐠
    @Override
    public List<IndexDataWithInfoDto> favoriteIndex(PeriodType periodType) {
        // {종가}를 기준으로 비교
        LocalDate now = LocalDate.from(LocalDateTime.now());
        LocalDate from = getPeriodTypeDate(periodType);

        List<IndexDataWithInfoDto> dooList = indexDataRepository.findAllFavoriteIndex(from, now);
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
}
