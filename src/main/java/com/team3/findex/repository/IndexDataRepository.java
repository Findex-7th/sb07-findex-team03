package com.team3.findex.repository;

import com.team3.findex.dto.indexDataDto.ChartDataPointDto;
import com.team3.findex.dto.indexDataDto.IndexDataWithInfoDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

    void deleteAllByIndexInfoId(Long indexInfoId); //!! for.IndexInfo
    void deleteByIndexInfoId(Long indexInfoId);


//    List<IndexData> findAllByIdInAndBaseDateBetween(List<Long> indexInfoIds, String baseDateFrom, String baseDateTo); //!! for. 성연

    //🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋
    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE d.indexInfo.id = :indexInfoId "
        + "AND d.baseDate >= :startDate "
        + "AND d.baseDate <= :endDate ")
//        + "AND d.closingPrice < :createdAt " //??)
    Slice<IndexData> findAllIndexDataWithIndexInfo(
        @Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("strCursor") String strCursor,
        Pageable pageable);


//    @Query(value = "SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.closingPrice) "
//        + "FROM IndexData d "
//        + "WHERE d.indexInfo.id = :id "
//        + "AND d.baseDate >= :startDate "
//        + "AND d.baseDate <= :endDate "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findChartData(@Param("id") Long id,
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate);
//
//    @Query(value = "SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma5) "
//        + "FROM IndexData d "
//        + "WHERE d.indexInfo.id = :id "
//        + "AND d.baseDate >= :startDate "
//        + "AND d.baseDate <= :endDate "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findMa5(@Param("id")Long id,
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate);
//
//    @Query(value = "SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma20 ) "
//        + "FROM IndexData d "
//        + "WHERE d.indexInfo.id = :id "
//        + "AND d.baseDate >= :startDate "
//        + "AND d.baseDate <= :endDate "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findMa20(@Param("id") Long id,
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate);


    //🐠🐠🐠주요 지수⭕️⭕️⭕️
    //    - **{즐겨찾기}**된 지수의 성과 정보를 포함합니다.
    //    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("""
    SELECT new com.team3.findex.dto.indexDataDto.IndexDataWithInfoDto(
        i.id,
        i.indexClassification,
        i.indexName,
        d.versus,
        d.fluctuationRate,
        d.closingPrice,
        d.closingPrice
    )
    FROM IndexData d
    JOIN d.indexInfo i
    WHERE d.baseDate >= :startDate
      AND d.baseDate <= :endDate
    ORDER BY d.closingPrice DESC
""")
    List<IndexDataWithInfoDto> findAllFavoriteIndex(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate); //?? 🚨periodType




    //🧊🧊🧊지수 성과 분석 랭킹 ⭕️⭕️⭕️
    //    - 전일/전주/전월 대비 성과 랭킹
    //    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("""
    SELECT new com.team3.findex.dto.indexDataDto.IndexDataWithInfoDto(
        i.id,
        i.indexClassification,
        i.indexName,
        d.versus,
        d.fluctuationRate,
        d.closingPrice,
        d.closingPrice
    )
    FROM IndexData d
    JOIN d.indexInfo i
    WHERE i.favorite = true
      AND d.baseDate >= :startDate
      AND d.baseDate <= :endDate
    ORDER BY d.closingPrice DESC
""")
    List<IndexDataWithInfoDto> findAllPerformanceRank( @Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable); //?? 🚨periodType


    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE i.id = :indexInfoId "
        + "AND d.baseDate >= :startDate "
        + "AND d.baseDate <= :endDate ")
//        + "ORDER BY d.baseDate ASC")
    List<IndexData> findAllExportCsvData(@Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Sort sort );
}