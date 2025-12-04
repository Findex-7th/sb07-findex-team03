package com.team3.findex.repository;

import com.team3.findex.domain.index.ChartPeriodType;
import com.team3.findex.domain.index.IndexDataUser;
import com.team3.findex.domain.index.IndexInfo;
import com.team3.findex.dto.indexDataDto.ChartDataPointDto;
import com.team3.findex.dto.indexDataDto.IndexChartDto;
import com.team3.findex.dto.indexDataDto.IndexPerformanceDto;
import com.team3.findex.dto.indexDataDto.RankedIndexPerformanceDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

    void deleteAllByIndexInfoId(Long indexInfoId); //!! for.IndexInfo

//    List<IndexData> findAllByIdInAndBaseDateBetween(List<Long> indexInfoIds, String baseDateFrom, String baseDateTo); //!! for. 성연


//    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//       + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.closingPrice) "
//       + "FROM IndexData d "
//       + "WHERE d.indexInfo.id = :id "
//       + "AND d.periodType = :chartPeriodType "
//       + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findChartData(Long id, ChartPeriodType chartPeriodType);
//
//    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma5) "
//        + "FROM IndexData d "
//        + "WHERE d.indexInfo.id = :id "
//        + "AND d.periodType = :chartPeriodType "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findMa5(Long id, ChartPeriodType chartPeriodType);
//
//    @Query("SELECT new com.team3.findex.dto.indexDataDto.ChartDataPointDto( "
//        + "FUNCTION('DATE_FORMAT', d.baseDate, '%Y-%m-%d'), d.ma20 ) "
//        + "FROM IndexData d "
//        + "WHERE d.indexInfo.id = :id "
//        + "AND d.periodType = :chartPeriodType "
//        + "ORDER BY d.baseDate ASC ")
//    List<ChartDataPointDto> findMa20(Long id, ChartPeriodType chartPeriodType);


    @Query("SELECT d "
        + "FROM IndexData d "
        + "JOIN FETCH d.indexInfo i "
        + "WHERE i.id = :indexInfoId ")
    Page<IndexData> findAllPerformanceRank(long indexInfoId, String periodType, Pageable pageable); //?? 🚨periodType


    @Query("SELECT new com.team3.findex.dto.indexDataDto.IndexPerformanceDto(i, d) "
    + "FROM IndexDataUser f "
    + "JOIN FETCH f.indexInfo i "
    + "JOIN FETCH IndexData d ON d.indexInfo.id = i.id "
    + "WHERE f.isFavorites = true ")
    List<IndexPerformanceDto> findAllPerformanceFavorite(@Param("id") ChartPeriodType chartPeriodType); //?? 🚨periodType


    @Query("SELECT d FROM IndexData d "
        + "JOIN FETCH IndexInfo i "
        + "WHERE i.id = :id "
        + "AND d.baseDate > :startDate "
        + "AND d.baseDate < :endDate ")
    List<IndexData> findAllExportCsvData(@Param("id") Long indexInfoId,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         Sort sort );
}



//public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
//
//    BooleanBuilder builder = new BooleanBuilder();
//    if (StringUtils.hasText(condition.getUsername())) {
//        builder.and(member.username.eq(condition.getTeamName()));
//    }
//    if (StringUtils.hasText(condition.getTeamName())) {
//        builder.and(team.name.eq(condition.getUsername()));
//    }
//
//    return queryFactory
//        .select(new QMemberTeamDto(
//            member.id.as("memberId"),
//            member.username,
//            team.id.as("teamId"),
//            team.name.as("teamName")))
//        .from(member)
//        .leftJoin(member.team, team)
//        .where(builder)
//        .fetch();
//}