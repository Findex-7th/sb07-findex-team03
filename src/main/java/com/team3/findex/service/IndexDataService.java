package com.team3.findex.service;

//import com.team3.findex.dto.indexDataDto.CursorPageResponse;
import com.team3.findex.dto.indexDataDto.CursorPageResponse;
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
import com.team3.findex.entity.index.ChartPeriodType;
import com.team3.findex.entity.index.IndexChart;
import com.team3.findex.entity.index.IndexData;
import com.team3.findex.mapper.IndexChartMapper;
import com.team3.findex.mapper.IndexDataMapper;
//import com.team3.findex.mapper.IndexPerformanceMapper;
//import com.team3.findex.mapper.RankedIndexPerformanceMapper;
import com.team3.findex.repository.IndexDataRepository;
import jakarta.transaction.Transactional;
//import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//
//        if (null == sortField) throw new IllegalArgumentException("🚨sortField is null");
//        if (null == sortDirection) throw new IllegalArgumentException("🚨 sortDirection is null");
//        if (null == size) throw new IllegalArgumentException("🚨size is null");
//
//        List<IndexDataDto> indexDataDtoList = indexDataRepository.getAllIndexData(sortField, sortDirection, size)
//            .stream()
//            .map(indexDataMapper::toDTO)
//            .toList();
//
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

//        indexDataRepository

        return;
    }
}
