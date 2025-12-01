package com.team3.findex.openapi;

import java.util.List;
import lombok.Getter;

/**
 * API 전체 응답 계층 구조
 * <p>
 * JSON 응답 매핑 클래스
 * </p>
 */
@Getter
public class ApiResponse {
  private Response response;

  @Getter
  public static class Response {
    private Body body;
  }

  @Getter
  public static class Body {
    private Items items;
  }

  @Getter
  public static class Items{
    private List<IndexApiResponse> item;
  }
}
