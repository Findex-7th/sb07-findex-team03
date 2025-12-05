package com.team3.findex.domain.syncjob.openApiTester.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseDto {

    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode; // 결과코드
        private String resultMsg;  // 결과메시지
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private int numOfRows;
        private int pageNo;
        private int totalCount;
        private Items items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JsonProperty("item")
        private List<ApiItemDto> itemList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiItemDto {
        // --- IndexInfo 관련 필드 ---
        private String idxNm;       // 지수명
        private String idxCsf;      // 지수분류명
        private Integer epyItmsCnt; // 채용종목 수
        private String basPntm;     // 기준시점 (YYYYMMDD)
        private Double basIdx;      // 기준지수

        // --- IndexData 관련 필드 ---
        private String basDt;           // 기준일자 (YYYYMMDD)
        private Double mkp;             // 시가
        private Double clpr;            // 종가
        private Double hipr;            // 고가
        private Double lopr;            // 저가
        private Long trqu;              // 거래량
        private Double vs;              // 대비 (등락폭)
        private Double fltRt;           // 등락률
        private Long trPrc;             // 거래대금
        private Long lstgMrktTotAmt;    // 상장시가총액
    }
}
