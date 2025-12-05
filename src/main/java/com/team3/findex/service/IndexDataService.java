package com.team3.findex.service;

//import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.common.exception.CustomException;
import com.team3.findex.common.exception.ErrorCode;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.PeriodType;
import com.team3.findex.domain.index.SourceType;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.IndexDataExcelDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataDto;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
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
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final IndexInfoRepository indexInfoRepository;

//    private final RankedIndexPerformanceMapper rankedIndexPerformanceMapper;
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
      IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId())
          .orElseThrow(() -> new CustomException(ErrorCode.INDEX_INFO_NOT_FOUND));

      IndexData indexData = new IndexData(
          indexInfo,
          request.marketPrice(),
          request.closingPrice(),
          request.highPrice(),
          request.lowPrice(),
          request.tradingQuantity(),
          request.versus(),
          request.fluctuationRate(),
          SourceType.USER,
          LocalDate.parse(request.baseDate()),
          request.tradingPrice(),
          request.marketTotalAmount()
      );

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
        // Open API를 활용

        IndexData indexData = indexDataRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("🚨 error - updateIndexData.id"));

        indexData.setUpdateIndexData(request);

        return indexDataMapper.toDTO(indexData);
    }


    @Transactional
    @Override
    public IndexChartDto getChartData(Long id, PeriodType periodType) {

//        IndexInfo indexInfo = indexInfoRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("🚨indexInfo.id error!"));
//
//        LocalDate now = LocalDate.from(LocalDateTime.now());
//        LocalDate from = getPeriodTypeDate(periodType);
//
//        List<ChartDataPointDto> data = indexDataRepository.findChartData(id, from, now,);
//        List<ChartDataPointDto> ma5 = indexDataRepository.findMa5(id, from, now,);
//        List<ChartDataPointDto> ma20 = indexDataRepository.findMa20(id, from, now,);
//
//        return new IndexChartDto(
//            indexInfo.getId(),
//            indexInfo.getIndexClassification(),
//            indexInfo.getIndexName(),
//            periodType.getValue(),
//            data,
//            ma5,
//            ma20
//        );

//        IndexChart indexChart = null;
        return indexChartMapper.toDTO(null);
    }


//
//    @Override
//    public List<RankedIndexPerformanceDto> performanceRank(long indexInfoId, String periodType, int limit) {
//
//        Pageable pageable = PageRequest.of(0, limit);
//        List<IndexData> indexDataPage = indexDataRepository.findAllPerformanceRank(indexInfoId, periodType, limit);
////        Page<IndexData> indexDataPage = indexDataRepository.findAllPerformanceRank(indexInfoId, periodType, pageable);
//
//        long startRank = pageable.getOffset() + 1;
//
//        List<IndexData> indexDataList = indexDataPage.getContent();
//        List<RankedIndexPerformanceDto> result = new ArrayList<>();
//        for (int i = 0; i < indexDataList.size(); i++) {
//            IndexData data = indexDataList.get(i);
//            IndexPerformanceDto performanceDto = IndexPerformanceDto.from(data);
//
//            int currentRank = (int) (startRank + i);
//            result.add(new RankedIndexPerformanceDto(performanceDto, currentRank));
//        }
//
//
//
    ////        List<IndexPerformanceDto> indexPerformanceDtoList = indexDataList
    ////            .stream()
    ////            .map(IndexPerformanceDto::from)
    ////            .toList();
//
//
//        return result;
//    }

    @Override
    public List<RankedIndexPerformanceDto> performanceRank(long indexInfoId, PeriodType periodType, int limit) {

        LocalDate now = LocalDate.from(LocalDateTime.now());
        LocalDate from = getPeriodTypeDate(periodType);

        List<IndexPerformanceDto> indexPerformanceDtoList = indexDataRepository.findAllPerformanceRank(indexInfoId, from, now, limit)
            .stream()
            .map(IndexPerformanceDto::fromIndexData)
            .toList();

        List<RankedIndexPerformanceDto> result = new ArrayList<>();
        for (int i = 0; i < indexPerformanceDtoList.size(); i++) {
            result.add(new RankedIndexPerformanceDto(indexPerformanceDtoList.get(i), i + 1));
        }

        return result;
    }

    @Override
    public List<IndexPerformanceDto> performanceFavorite(PeriodType periodType) {
        // {종가}를 기준으로 비교

        LocalDate now = LocalDate.from(LocalDateTime.now());
        LocalDate from = getPeriodTypeDate(periodType);

        return indexDataRepository.findAllPerformanceFavorite(from, now)
            .stream()
            .map(IndexPerformanceDto::fromFavoriteDto)
            .toList();
    }



    @Override
    public List<IndexDataExcelDto> exportCsv(  Long indexInfoId,
                            String startDate,
                            String endDate,
                            String sortField,
                            String sortDirection
                        ) {

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

      return indexDataList.stream()
          .map(indexDataMapper::toExcelDto)
          .toList();
    }


    private LocalDate getPeriodTypeDate(PeriodType periodType) {
        LocalDate fromData = LocalDate.now();

        switch (periodType) {
            case DAILY -> fromData = fromData.minusDays(1);
            case WEEKLY -> fromData = fromData.minusWeeks(1);
            case MONTHLY -> fromData = fromData.minusMonths(1);
        }

        return fromData;
    }
}
