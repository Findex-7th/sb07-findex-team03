package com.team3.findex.repository;

import com.team3.findex.domain.index.PeriodType;
import com.team3.findex.dto.indexDataDto.ChartDataPointDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

    void deleteAllByIndexInfoId(Long indexInfoId); //!! for.IndexInfo

//    List<IndexData> findAllByIdInAndBaseDateBetween(List<Long> indexInfoIds, String baseDateFrom, String baseDateTo); //!! for. 성연

//🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋

    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
       + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.closingPrice) "
       + "FROM IndexData d "
       + "WHERE d.indexInfo.id = :id "
       + "AND d.periodType = :chartPeriodType "
       + "ORDER BY d.baseDate ASC ")
    List<ChartDataPointDto> findChartData(Long id, PeriodType periodType);

    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma5) "
        + "FROM IndexData d "
        + "WHERE d.indexInfo.id = :id "
        + "AND d.periodType = :chartPeriodType "
        + "ORDER BY d.baseDate ASC ")
    List<ChartDataPointDto> findMa5(Long id, PeriodType periodType);

    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma20 ) "
        + "FROM IndexData d "
        + "WHERE d.indexInfo.id = :id "
        + "AND d.periodType = :chartPeriodType "
        + "ORDER BY d.baseDate ASC ")
    List<ChartDataPointDto> findMa20(Long id, PeriodType periodType);


//    - **{즐겨찾기}**된 지수의 성과 정보를 포함합니다.
//    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("SELECT d "
        + "FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE i.id = :indexInfoId "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate < :endDate "
        + "ORDER BY d.closingPrice DESC LIMIT :limit")
    List<IndexData> findAllPerformanceRank( long indexInfoId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            int limit); //?? 🚨periodType
//    Page<IndexData> findAllPerformanceRank(long indexInfoId, String periodType, Pageable pageable); //?? 🚨periodType



//    - 전일/전주/전월 대비 성과 랭킹
//    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("SELECT d "
        + "FROM IndexDataUser f "
        + "JOIN FETCH f.indexInfo i "
        + "JOIN FETCH IndexData d ON d.indexInfo.id = i.id "
        + "WHERE f.isFavorites = true "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate < :endDate "
        + "ORDER BY d.closingPrice DESC")
    List<IndexData> findAllPerformanceFavorite(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate); //?? 🚨periodType





    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE i.id = :id "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate < :endDate ")
//        + "ORDER BY d.baseDate ASC")
    List<IndexData> findAllExportCsvData(@Param("id") Long indexInfoId,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         Sort sort );
}