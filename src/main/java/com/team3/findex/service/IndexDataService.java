package com.team3.findex.service;

//import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.dto.indexDataDto.ChartDataPointDto;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.IndexDataExcelDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.ExportCsvRequest;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
//import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
//import com.team3.findex.entity.index.IndexPerformance;
//import com.team3.findex.entity.index.RankedIndexPerformance;
import com.team3.findex.domain.index.ChartPeriodType;
import com.team3.findex.domain.index.IndexChart;
import com.team3.findex.domain.index.IndexData;
import com.team3.findex.domain.index.mapper.IndexChartMapper;
import com.team3.findex.domain.index.mapper.IndexDataMapper;
//import com.team3.findex.mapper.IndexPerformanceMapper;
//import com.team3.findex.mapper.RankedIndexPerformanceMapper;
import com.team3.findex.repository.IndexDataRepository;
import com.team3.findex.repository.IndexInfoRepository;
import com.team3.findex.service.Interface.IndexDataServiceInterface;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDataService implements IndexDataServiceInterface {
    private final IndexDataRepository indexDataRepository;
    private final IndexInfoRepository indexInfoRepository;

//    private  final RankedIndexPerformanceMapper rankedIndexPerformanceMapper;
//    private final IndexPerformanceMapper indexPerformanceMapper;
    private final IndexDataMapper indexDataMapper;
    private final IndexChartMapper indexChartMapper;

    @Override
    public CursorPageResponse<IndexDataDto> getAllIndexData(String sortField, String sortDirection, Integer size) {

        if (null == sortField) throw new IllegalArgumentException("🚨sortField is null");
        if (null == sortDirection) throw new IllegalArgumentException("🚨 sortDirection is null");
        if (null == size) throw new IllegalArgumentException("🚨size is null");

        // 커서 페이지
//        List<IndexDataDto> indexDataDtoList = indexDataRepository.getAllIndexData(sortField, sortDirection, size)
//            .stream()
//            .map(indexDataMapper::toDTO)
//            .toList();

//        return new CursorPageResponse<indexDataDto>(); //??
        return null;
    }


    @Transactional
    @Override
    public IndexDataDto createIndexData(IndexDataCreateRequest request) {

        IndexData indexData = indexDataMapper.toEntity(request);

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

    @Transactional
    @Override
    public IndexChartDto getChartData(Long id, ChartPeriodType chartPeriodType) {

        IndexInfo indexInfo = indexInfoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("🚨indexInfo.id error!"));

//        List<ChartDataPointDto> data = indexDataRepository.findChartData(id, chartPeriodType);
//        List<ChartDataPointDto> ma5 = indexDataRepository.findMa5(id, chartPeriodType);
//        List<ChartDataPointDto> ma20 = indexDataRepository.findMa20(id, chartPeriodType);
//
//        return new IndexChartDto(
//            indexInfo.getId(),
//            indexInfo.getIndexClassification(),
//            indexInfo.getIndexName(),
//            chartPeriodType.getValue(),
//            data,
//            ma5,
//            ma20
//        );

//        IndexChart indexChart = null;
//        return indexChartMapper.toDTO(indexChart);
        return null;
    }


    @Override
    public List<RankedIndexPerformanceDto> performanceRank(long indexInfoId, String periodType, int limit) {

        Pageable pageable = PageRequest.of(0, limit);
        Page<IndexData> indexDataPage = indexDataRepository.findAllPerformanceRank(indexInfoId,
            periodType, pageable);

        long startRank = pageable.getOffset() + 1;

        List<IndexData> content = indexDataPage.getContent();
        List<RankedIndexPerformanceDto> result = new ArrayList<>();

        for (int i = 0; i < content.size(); i++) {
            IndexData data = content.get(i);
            IndexPerformanceDto performanceDto = IndexPerformanceDto.from(data.getIndexInfo(),
                data);

            int currentRank = (int) (startRank + i);
            result.add(new RankedIndexPerformanceDto(performanceDto, currentRank));
        }

        return result;
    }


    @Override
    public List<IndexPerformanceDto> performanceFavorite(ChartPeriodType chartPeriodType) {

        return indexDataRepository.findAllPerformanceFavorite(chartPeriodType);
    }


    @Override
    public void exportCsv(  Long indexInfoId,
                            String startDate,
                            String endDate,
                            String sortField,
                            String sortDirection
                        ) {

        //?? 내 맘대로 하루치??!!
        if (startDate.isEmpty() || startDate.isBlank())
            startDate = String.valueOf(LocalDate.now());

        if (endDate.isEmpty() || endDate.isBlank())
            endDate = String.valueOf(LocalDate.now());

        sortField = "baseDate";

        Sort.Order order = (0 != sortDirection.compareTo("desc")) ? Order.desc(sortField) : Order.asc(sortField);

        List<IndexData> indexDataList = indexDataRepository.findAllExportCsvData(indexInfoId,
                                                            startDate,
                                                            endDate,
                                                            Sort.by(order));

        if (indexDataList.isEmpty()) throw new NoSuchElementException("🚨해당하는 엑셀 자료 없음");

        List<IndexDataExcelDto> excelDtos = indexDataList.stream()
            .map(indexDataMapper::toExcelDto)
            .toList();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("index-data");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("기준일자");
            header.createCell(1).setCellValue("시가");
            header.createCell(2).setCellValue("종가");
            header.createCell(3).setCellValue("고가");
            header.createCell(4).setCellValue("저가");
            header.createCell(5).setCellValue("전일 대비 등락폭");
            header.createCell(6).setCellValue("등락률");
            header.createCell(7).setCellValue("거래량");
            header.createCell(8).setCellValue("거래대금");
            header.createCell(9).setCellValue("상장시가총액");

            int rowIdx = 1;
            for (IndexDataExcelDto dto : excelDtos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.baseDate().toString());
                row.createCell(1).setCellValue(dto.marketPrice().doubleValue());
                row.createCell(2).setCellValue(dto.closingPrice().doubleValue());
                row.createCell(3).setCellValue(dto.highPrice().doubleValue());
                row.createCell(4).setCellValue(dto.lowPrice().doubleValue());
                row.createCell(5).setCellValue(dto.versus().doubleValue());
                row.createCell(6).setCellValue(dto.fluctuationRate().doubleValue());
                row.createCell(7).setCellValue(dto.tradingQuantity().doubleValue());
                row.createCell(8).setCellValue(dto.tradingPrice().doubleValue());
                row.createCell(9).setCellValue(dto.marketTotalAmount().doubleValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException("🚨파일 다운로드 실패");
        }
    }
}
