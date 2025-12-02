package com.team3.findex.IndexData.entity;

import com.team3.findex.IndexData.common.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
public class IndexData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "index_info_id")
    private Long indexInfoId; // 지수ID

    @Column(name = "marke_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal marketPrice; // 시가

    @Column(name = "closing_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal closingPrice; //	종가

    @Column(name = "high_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal highPrice; //	고가

    @Column(name = "low_price", precision = 6, scale = 2, nullable = false)
    private BigDecimal lowPrice; //	저가

    @JoinColumn(name = "trading_quantity")
    private Long tradingQuantity; // 거래량

    @Column(name = "variation_sign", precision = 6, scale = 2, nullable = false)
    private BigDecimal versus; //	전일 대비 등락폭

    @Column(name = "fluctuation_rate", precision = 6, scale = 2, nullable = false)
    private BigDecimal fluctuationRate; //	등락률

    @Column(name = "source_type", nullable = false)
    private SourceType sourceType; // 소스 타입

    @Column(name = "base_point_time", nullable = false)
    private Instant baseDate; // 기준일자
}
