package com.team3.findex.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

    void deleteAllByIndexInfoId(Long indexInfoId); //!! for.IndexInfo

    List<IndexData> findAllByIdInAndBaseDateBetween(List<Long> indexInfoIds, String baseDateFrom, String baseDateTo); //!! for. 성연

//    @Query("SELECT i FROM IndexData i ORDER BY i.indexInfo.indexName ASC LIMIT '{'limit'}'") //????
//    SELECT * FROM users
//    WHERE id > :cursor
//    ORDER BY id ASC
//    LIMIT :limit;
    // 커서 페이지
//    List<IndexData> getAllIndexData(String sortField, String sortDirection, Integer size);

//    IndexData findByIdAndPeriodType(Long id, ChartPeriodType chartPeriodType);


    @Query("SELECT i. FROM IndexData d "
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