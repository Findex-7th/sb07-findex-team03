package com.team3.findex.common.openapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

/**
 * 금융위원회 지수시세정보 API 응답 class
 * <p>
 * API로부터 받은 개별 지수 데이터를 담는 클래스
 * </p>
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
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
  private BigDecimal closingPrice;

  @JsonProperty("vs")
  private BigDecimal variationSign;

  @JsonProperty("fltRt")
  private BigDecimal changeRate;

  @JsonProperty("mkp")
  private BigDecimal marketPrice;

  @JsonProperty("hipr")
  private BigDecimal highPrice;

  @JsonProperty("lopr")
  private BigDecimal lowPrice;

  @JsonProperty("trqu")
  private Long tradeVolume;

  @JsonProperty("trPrc")
  private Long tradePrice;

  @JsonProperty("lstgMrktTotAmt")
  private Long marketTotalAmount;
}
