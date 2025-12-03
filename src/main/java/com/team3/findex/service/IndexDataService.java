package com.team3.findex.service;

//import com.team3.findex.dto.indexDataDto.CursorPageResponse;
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
import com.team3.findex.service.Interface.IndexDataServiceInterface;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDataService implements IndexDataServiceInterface {
    private final IndexDataRepository indexDataRepository;

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

//        IndexData indexData = indexDataRepository.findByIdAndPeriodType(id, chartPeriodType);

        IndexChart indexChart = null;
        return indexChartMapper.toDTO(indexChart);
    }

    @Override
    public RankedIndexPerformanceDto performanceRank(long indexInfoId, String periodType, int limit) {
//
////        indexDataRepository
//
//        IndexPerformance performance = null;
//        RankedIndexPerformance rankedIndexPerformance = null;
//
//        return rankedIndexPerformanceMapper.toDTO(rankedIndexPerformance);
        return null;
    }

    @Override
    public IndexPerformanceDto performanceFavorite(ChartPeriodType chartPeriodType) {

//        indexDataRepository

        IndexPerformanceDto indexPerformance = null;

        return null; //indexPerformanceMapper.toDTO(indexPerformance);
    }

    @Override
    public void exportCsv(ExportCsvRequest request) {

        Sort sort = Sort.by(Order.desc("city"));

        List<IndexData> indexDataList = indexDataRepository.findAllExportCsvData( request.indexInfoId(),
                                                            request.startDate(),
                                                            request.endDate(),
                                                            sort);

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

            // 메모리에 파일 저장 후 반환
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("🚨파일 다운로드 실패");
        }
    }
}
