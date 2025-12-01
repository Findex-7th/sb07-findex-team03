package com.team3.findex.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 금융위원회 지수시세정보 API 응답 class
 * <p>
 * API로부터 받은 개별 지수 데이터를 담는 클래스
 * </p>
 */
@Getter
public class IndexApiResponse {

  @JsonProperty("basPntm")
  private String basePoint;

  @JsonProperty("basIdx")
  private String baseIndex;

  @JsonProperty("basDt")
  private String baseDate;

  @JsonProperty("idxCsf")
  private String indexCategory;

  @JsonProperty("idxNm")
  private String indexName;

  @JsonProperty("epyItmsCnt")
  private Integer itemsCount;

  @JsonProperty("clpr")
  private Double closingPrice;

  @JsonProperty("vs")
  private Double variationSign;

  @JsonProperty("fltRt")
  private Double changeRate;

  @JsonProperty("mkp")
  private Double marketPrice;

  @JsonProperty("hipr")
  private Double highPrice;

  @JsonProperty("lopr")
  private Double lowPrice;

  @JsonProperty("trqu")
  private Long tradeVolume;

  @JsonProperty("trPrc")
  private Long tradePrice;

  @JsonProperty("lstgMrktTotAmt")
  private Long marketTotalAmount;
}
