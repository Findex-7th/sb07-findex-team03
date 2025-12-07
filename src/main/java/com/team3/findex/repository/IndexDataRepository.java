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
        + "AND d.baseDate > :startDate "
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
//        + "AND d.baseDate > :startDate "
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
//        + "AND d.baseDate > :startDate "
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
//        + "AND d.baseDate > :startDate "
//        + "AND d.baseDate <= :endDate "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findMa20(@Param("id") Long id,
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate);

//    @Query(value =
//        "SELECT DATE_FORMAT(t.base_date, '%Y-%m-%d') AS baseDate, " +
//            "       t.ma5 AS value " +
//            "FROM ( " +
//            "    SELECT d.base_date, " +
//            "           AVG(d2.closing_price) AS ma5 " +
//            "    FROM index_data d " +
//            "    JOIN index_data d2 " +
//            "      ON d2.index_info_id = d.index_info_id " +
//            "     AND d2.base_date BETWEEN DATE_SUB(d.base_date, INTERVAL 4 DAY) AND d.base_date " +
//            "    WHERE d.index_info_id = :id " +
//            "      AND d.base_date BETWEEN :startDate AND :endDate " +
//            "    GROUP BY d.base_date " +
//            ") t " +
//            "ORDER BY t.base_date ASC",
//        nativeQuery = true)
//    List<ChartDataPointDto> findMa5(
//        @Param("id") Long id,
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate);

//🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷

    @Query(value =
        "SELECT FORMATDATETIME(d.base_date, 'yyyy-MM-dd') AS date, d.ma5 AS value " +
            "FROM index_data d " +
            "WHERE d.index_info_id = :id " +
            "  AND d.base_date BETWEEN :startDate AND :endDate " +
            "ORDER BY d.base_date ASC",
        nativeQuery = true)
    List<ChartDataPointDto> findMa5(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query(value =
        "SELECT FORMATDATETIME(d.base_date, 'yyyy-MM-dd') AS date, d.ma20 AS value " +
            "FROM index_data d " +
            "WHERE d.index_info_id = :id " +
            "  AND d.base_date BETWEEN :startDate AND :endDate " +
            "ORDER BY d.base_date ASC",
        nativeQuery = true)
    List<ChartDataPointDto> findMa20(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


    @Query(value =
        "SELECT CONCAT(x.y, '-', LPAD(x.m, 2, '0')) AS period, d.closing_price AS value " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_date) AS y, MONTH(base_date) AS m, MAX(base_date) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_date BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_date), MONTH(base_date) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND d.base_date = x.last_day " +
            "ORDER BY d.base_date ASC",
        nativeQuery = true)
    List<ChartDataPointDto> findMonthlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


    @Query(value =
        "SELECT CONCAT(x.y, '-Q', x.q) AS period, d.closing_price AS value " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_date) AS y, QUARTER(base_date) AS q, MAX(base_date) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_date BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_date), QUARTER(base_date) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND d.base_date = x.last_day " +
            "ORDER BY d.base_date ASC",
        nativeQuery = true)
    List<ChartDataPointDto> findQuarterlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


    @Query(value =
        "SELECT CAST(x.y AS VARCHAR) AS period, d.closing_price AS value " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_date) AS y, MAX(base_date) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_date BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_date) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND d.base_date = x.last_day " +
            "ORDER BY d.base_date ASC",
        nativeQuery = true)
    List<ChartDataPointDto> findYearlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);



//🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼
//🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼

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
    WHERE d.baseDate > :startDate
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
         MIN(i.id),
         i.indexClassification,
         i.indexName,
         SUM(d.versus),
         SUM(d.fluctuationRate),
         SUM(d.closingPrice),
         SUM(d.closingPrice - d.versus)
    )
    FROM IndexData d
    JOIN d.indexInfo i
    WHERE i.favorite = true
      AND d.baseDate > :startDate
      AND d.baseDate <= :endDate
    GROUP BY i.indexClassification, i.indexName
    ORDER BY SUM(d.closingPrice) DESC
""")
    List<IndexDataWithInfoDto> findAllPerformanceRank( @Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable); //?? 🚨periodType


    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE i.id = :indexInfoId "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate <= :endDate ")
//        + "ORDER BY d.baseDate ASC")
    List<IndexData> findAllExportCsvData(@Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Sort sort );
}