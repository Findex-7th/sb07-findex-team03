package com.team3.findex.domain.index.repository;

import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.domain.index.dto.IndexDataWithInfoDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long>, IndexDataRepositoryCustom {

    void deleteAllByIndexInfoId(Long indexInfoId); //!! for.IndexInfo
    void deleteByIndexInfoId(Long indexInfoId);

    //🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋🍋
    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE d.indexInfo.id = :indexInfoId "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate <= :endDate ")
    Slice<IndexData> findAllIndexDataWithIndexInfo(
        @Param("indexInfoId") Long indexInfoId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("strCursor") String strCursor,
        Pageable pageable);


//🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷

    @Query(value =
        "SELECT FORMATDATETIME(t.base_point_time, 'yyyy-MM-dd') AS date, t.ma5 AS val " +
            "FROM ( " +
            "    SELECT d.base_point_time, AVG(d2.closing_price) AS ma5 " +
            "    FROM index_data d " +
            "    JOIN index_data d2 " +
            "      ON d2.index_info_id = d.index_info_id " +
            "     AND d2.base_point_time BETWEEN DATEADD('DAY', -4, d.base_point_time) AND d.base_point_time " +
            "    WHERE d.index_info_id = :id " +
            "      AND d.base_point_time BETWEEN :startDate AND :endDate " +
            "    GROUP BY d.base_point_time " +
            ") t " +
            "ORDER BY t.base_point_time ASC",
        nativeQuery = true)
    List<Object[]> findMa5(
        @Param("id") Long id,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);


    @Query(value =
        "SELECT FORMATDATETIME(t.base_point_time, 'yyyy-MM-dd') AS date, t.ma20 AS val " +
            "FROM ( " +
            "    SELECT d.base_point_time, AVG(d2.closing_price) AS ma20 " +
            "    FROM index_data d " +
            "    JOIN index_data d2 " +
            "      ON d2.index_info_id = d.index_info_id " +
            "     AND d2.base_point_time BETWEEN DATEADD('DAY', -19, d.base_point_time) AND d.base_point_time " +
            "    WHERE d.index_info_id = :id " +
            "      AND d.base_point_time BETWEEN :startDate AND :endDate " +
            "    GROUP BY d.base_point_time " +
            ") t " +
            "ORDER BY t.base_point_time ASC",
        nativeQuery = true)
    List<Object[]> findMa20(
        @Param("id") Long id,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);


    @Query(value =
        "SELECT CONCAT(x.y, '-', LPAD(x.m, 2, '0')) AS period, d.closing_price AS val " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_point_time) AS y, MONTH(base_point_time) AS m, " +
            "              MAX(base_point_time) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_point_time BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_point_time), MONTH(base_point_time) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND d.base_point_time = x.last_day " +
            "ORDER BY x.y ASC, x.m ASC",
        nativeQuery = true)
    List<Object[]> findMonthlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);


    @Query(value =
        "SELECT CONCAT(x.y, '-Q', x.q) AS period, d.closing_price AS val " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_point_time) AS y, " +
            "              (FLOOR((MONTH(base_point_time)-1)/3) + 1) AS q, " +
            "              MAX(base_point_time) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_point_time BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_point_time), (FLOOR((MONTH(base_point_time)-1)/3) + 1) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND YEAR(d.base_point_time) = x.y " +
            " AND (FLOOR((MONTH(d.base_point_time)-1)/3) + 1) = x.q " +
            " AND d.base_point_time = x.last_day " +
            "ORDER BY x.y ASC, x.q ASC",
        nativeQuery = true)
    List<Object[]> findQuarterlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);


    @Query(value =
        "SELECT CAST(x.y AS VARCHAR) AS period, d.closing_price AS val " +
            "FROM index_data d " +
            "JOIN ( " +
            "       SELECT index_info_id, YEAR(base_point_time) AS y, MAX(base_point_time) AS last_day " +
            "       FROM index_data " +
            "       WHERE index_info_id = :id " +
            "         AND base_point_time BETWEEN :startDate AND :endDate " +
            "       GROUP BY index_info_id, YEAR(base_point_time) " +
            ") x " +
            "  ON d.index_info_id = x.index_info_id " +
            " AND d.base_point_time = x.last_day " +
            "ORDER BY x.y ASC",
        nativeQuery = true)
    List<Object[]> findYearlySeries(
        @Param("id") Long id,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);





//🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼
//🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼🌼

    //🐠🐠🐠주요 지수⭕️⭕️⭕️
    //    - **{즐겨찾기}**된 지수의 성과 정보를 포함합니다.
    //    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("""
        SELECT new com.team3.findex.domain.index.dto.IndexDataWithInfoDto(
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




    // TODO 🧊🧊🧊지수 성과 분석 랭킹 ⭕️⭕️⭕️
    //    - 전일/전주/전월 대비 성과 랭킹
    //    - 성과는 **{종가}**를 기준으로 비교합니다.
    @Query("""
    SELECT new com.team3.findex.domain.index.dto.IndexDataWithInfoDto(
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

    Optional<IndexData> findByIndexInfoAndBaseDate(IndexInfo indexInfo, LocalDate baseDate);

    List<IndexData> findAllByIndexInfoAndBaseDateBetween(IndexInfo indexInfo, LocalDate baseDateFrom, LocalDate baseDateTo);
}