package com.team3.findex.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team3.findex.domain.index.IndexData;

public interface IndexDataRepository extends JpaRepository<IndexData, Long> {
//    @Query("SELECT i FROM IndexData i ORDER BY i.indexInfo.indexName ASC LIMIT '{'limit'}'") //????
//    SELECT * FROM users
//    WHERE id > :cursor
//    ORDER BY id ASC
//    LIMIT :limit;
//    List<IndexData> getAllIndexData(String sortField, String sortDirection, Integer size);

//    IndexData findByIdAndPeriodType(Long id, ChartPeriodType chartPeriodType);
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