package com.team3.findex.domain.index.service;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.ChartDataPointDto;
import com.team3.findex.domain.index.dto.IndexChartDto;
import com.team3.findex.domain.index.dto.IndexDataWithInfoDto;
import com.team3.findex.domain.index.dto.RankedIndexPerformanceDto;
import com.team3.findex.domain.index.enums.ChartPeriodType;
import com.team3.findex.domain.index.enums.PeriodType;
import com.team3.findex.domain.index.mapper.IndexChartMapper;
import com.team3.findex.domain.index.repository.IndexDataRepository;
import com.team3.findex.domain.index.repository.IndexInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndexChartServiceImpl implements IndexChartService{

    private final IndexChartMapper indexChartMapper;
    private final IndexDataRepository indexDataRepository;
    private final IndexInfoRepository indexInfoRepository;


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

    /**
     * *지수 성과 분석 랭킹** 전일/전주/전월 대비 성과 랭킹 성과는 **{종가}**를 기준으로 비교합니다.
     * 🧊🧊🧊지수 성과 분석 랭킹 🧊🧊🧊🧊
     *
     * @return
     */
    @Override
    public List<RankedIndexPerformanceDto> performanceRank(Long indexInfoId, PeriodType periodType,
                                                           int limit) {
        LocalDate end = LocalDate.from(LocalDateTime.now());
        LocalDate start = getPeriodTypeDate(periodType);

        List<IndexDataWithInfoDto> indexDataWithInfoDtoList = null;

        if (null == indexInfoId) {

            indexDataWithInfoDtoList = indexDataRepository
                    .findAllPerformanceRank(start, end, PageRequest.of(0, limit))
                    .stream()
                    .map(dto -> {
                        double value = dto.closingPriceAvg().doubleValue() - dto.versusAvg().doubleValue();
                        return IndexDataWithInfoDto.fromBeforeDto(dto, value);
                    })
                    .toList();
        } else {
            indexDataWithInfoDtoList = indexDataRepository
                    .findAllPerformanceRank(indexInfoId, start, end, PageRequest.of(0, limit))
                    .stream()
                    .map(dto -> {
                        double value = dto.closingPriceAvg().doubleValue() - dto.versusAvg().doubleValue();
                        return IndexDataWithInfoDto.fromBeforeDto(dto, value);
                    })
                    .toList();
        }


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

        LocalDateTime now = LocalDate.from(LocalDateTime.now()).atStartOfDay();
        LocalDateTime from = getChartPeriodTypeDate(periodType).atStartOfDay();
        List<ChartDataPointDto> ma5 = indexDataRepository.findMa5(id, from, now)
                .stream()
                .map(row -> new ChartDataPointDto(
                        (String) row[0],                     // date
                        ((BigDecimal) row[1]).doubleValue()  // value
                ))
                .toList();

        log.info("🐳 ma5 = " + String.valueOf(ma5.size()));

        List<ChartDataPointDto> ma20 = indexDataRepository.findMa20(id, from, now)
                .stream()
                .map(row -> new ChartDataPointDto(
                        (String) row[0],                     // date
                        ((BigDecimal) row[1]).doubleValue()  // value
                ))
                .toList();

        log.info("🐳 ma20 = " + String.valueOf(ma20.size()));

        List<ChartDataPointDto> pointDtoList = new ArrayList<>();
        switch (periodType) {
            case MONTHLY -> pointDtoList = indexDataRepository.findMonthlySeries(id, from, now)
                    .stream()
                    .map(row -> new ChartDataPointDto(
                            (String) row[0],                     // date
                            ((BigDecimal) row[1]).doubleValue()  // value
                    ))
                    .toList();
            case QUARTERLY -> pointDtoList = indexDataRepository.findQuarterlySeries(id, from, now)
                    .stream()
                    .map(row -> new ChartDataPointDto(
                            (String) row[0],                     // date
                            ((BigDecimal) row[1]).doubleValue()  // value
                    ))
                    .toList();
            case YEARLY -> pointDtoList = indexDataRepository.findYearlySeries(id, from, now)
                    .stream()
                    .map(row -> new ChartDataPointDto(
                            (String) row[0],                     // date
                            ((BigDecimal) row[1]).doubleValue()  // value
                    ))
                    .toList();
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
}
