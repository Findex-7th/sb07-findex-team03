package com.team3.findex.domain.index;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "index_info", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_index_classification_name",
                columnNames = {"index_classification", "index_name"}
        )
})
public class IndexInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 150)
  private String indexClassification;

  @Column(nullable = false, length = 150)
  private String indexName;

  @Column(nullable = false)
  private Integer employedItemsCount;

  @Column(nullable = false)
  private LocalDate basePointInTime;

  @Column(nullable = false)
  private Double baseIndex;

  @Column(nullable = false)
  private Boolean favorite = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SourceType sourceType;

  public IndexInfo(
      String indexClassification,
      String indexName,
      Integer employedItemsCount,
      LocalDate basePointInTime,
      Double baseIndex,
      Boolean favorite,
      SourceType sourceType
  ) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.employedItemsCount = employedItemsCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.favorite = favorite;
    this.sourceType = sourceType;
  }


  public void update(
      Integer employedItemsCount,
      LocalDate basePointInTime,
      Double baseIndex,
      Boolean favorite
  ) {
    if(employedItemsCount != null) { this.employedItemsCount = employedItemsCount; }
    if(basePointInTime != null) { this.basePointInTime = basePointInTime; }
    if(baseIndex != null) { this.baseIndex = baseIndex; }
    if(favorite != null && !favorite.equals(this.favorite)) { this.favorite = favorite; }
  }
}
