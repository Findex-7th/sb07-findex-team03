package com.team3.findex.domain.index.test;
// 테스트용으로 만들었어요

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AutoSyncConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long indexInfoId;

  @Column(nullable = false)
  private String indexClassification;

  @Column(nullable = false)
  private String indexName;

  @Column(nullable = false)
  private Boolean enabled;

  public AutoSyncConfig(Long indexInfoId, String indexClassification, String indexName, Boolean enabled) {
    this.indexInfoId = indexInfoId;
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.enabled = enabled;
  }
}