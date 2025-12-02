package com.team3.findex.common.openapi.service;

import com.team3.findex.common.openapi.IndexApiResponse;
import com.team3.findex.common.openapi.dto.IndexSyncData;
import com.team3.findex.common.openapi.dto.IndexInfoSyncData;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * API 데이터 제공 서비스 클래스
 * <p>
 * API로부터 받은 응답을 도메인 서비스에서 사용하기 편한 형태로 변환
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OpenApiProvider {

  private final OpenApiService openApiService;

  /**
   * 지수 정보 조회
   * @param date 조회할 기준 일자
   * @return 지수 정보 반환
   */
  public IndexInfoSyncData getIndexInfo(LocalDate date) {
    IndexApiResponse data = openApiService.getOpenApiDataByDate2(date);

    IndexInfoSyncData indexInfoData = new IndexInfoSyncData(
        data.getIndexCategory(),
        data.getIndexName(),
        data.getItemsCount(),
        LocalDate.parse(data.getBasePoint(), DateTimeFormatter.ofPattern("yyyyMMdd")),
        Double.parseDouble(data.getBaseIndex())
    );
    return indexInfoData;
  }

  /**
   * 지수 데이터 조회
   * @param date 조회할 기준 일자
   * @return 지수 데이터 반환
   */
  public IndexSyncData getIndexData(LocalDate date) {

    IndexApiResponse data = openApiService.getOpenApiDataByDate2(date);

    IndexSyncData indexData = new IndexSyncData(
        LocalDate.parse(data.getBaseDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
        data.getMarketPrice(),
        data.getClosingPrice(),
        data.getHighPrice(),
        data.getLowPrice(),
        data.getTradeVolume(),
        data.getVariationSign(),
        data.getChangeRate(),
        data.getTradePrice(),
        data.getMarketTotalAmount()
    );
    return indexData;
  }
}
