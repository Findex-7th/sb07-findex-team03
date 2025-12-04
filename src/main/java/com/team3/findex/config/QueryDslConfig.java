package com.team3.findex.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL 설정 클래스
 * <p>JPAQueryFactory를 Bean 등록하여 QueryDSL 사용</p>
 */
@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

  private final EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }


}
