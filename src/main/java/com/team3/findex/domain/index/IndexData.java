package com.team3.findex.domain.index;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
public class IndexData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id")
    private IndexInfo indexInfo; // 지수ID

    @Column(name = "marke_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal marketPrice; // 시가

    @Column(name = "closing_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal closingPrice; //	종가

    @Column(name = "high_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal highPrice; //	고가

    @Column(name = "low_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal lowPrice; //	저가

    @Column(name = "trading_quantity")
    private Long tradingQuantity; // 거래량

    @Column(name = "variation_sign", precision = 6, scale = 2, nullable = false)
    private BigDecimal versus; //	전일 대비 등락폭

    @Column(name = "fluctuation_rate", precision = 6, scale = 2, nullable = false)
    private BigDecimal fluctuationRate; //	등락률

    @Column(name = "source_type", nullable = false)
    private SourceType sourceType; // 소스 타입

    @Column(name = "base_point_time", nullable = false)
    private LocalDate baseDate; // 기준일자

    @Column(name = "trading_price", nullable = false)
    private Long tradingPrice; //거래대금

    @Column(name = "market_total_amount", nullable = false)
    private Long marketTotalAmount; //상장시가총액
}
