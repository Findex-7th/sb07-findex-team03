package com.team3.findex.domain.index;

import com.team3.findex.dto.indexDataDto.IndexDataCreateRequest;
import com.team3.findex.dto.indexDataDto.IndexDataUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity @Table(name ="IndexData")
public class IndexData extends IndexDataBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "index_info_id")
    @NotNull(message = "🚨indexInfo 필수입니다.")
    private IndexInfo indexInfo; // 지수ID

    @Column(name = "marke_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "🚨marketPrice 필수입니다.")
    private BigDecimal marketPrice; // 시가

    @Column(name = "closing_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "🚨closingPrice 필수입니다.")
    private BigDecimal closingPrice; //	종가

    @Column(name = "high_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "🚨highPrice 필수입니다.")
    private BigDecimal highPrice; //	고가

    @Column(name = "low_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "🚨lowPrice 필수입니다.")
    private BigDecimal lowPrice; //	저가

    @Column(name = "trading_quantity", precision = 20, nullable = false)
    @NotNull(message = "🚨tradingQuantity 필수입니다.")
    private BigDecimal tradingQuantity; // 거래량

    @Column(name = "variation_sign", precision = 6, scale = 2, nullable = false)
    @NotNull(message = "🚨versus 필수입니다.")
    private BigDecimal versus; //	전일 대비 등락폭

    @Column(name = "fluctuation_rate", precision = 6, scale = 2, nullable = false)
    @NotNull(message = "🚨fluctuationRate 필수입니다.")
    private BigDecimal fluctuationRate; //	등락률

    @Column(name = "source_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SourceType sourceType = SourceType.OPEN_API; // 소스 타입

    @Column(name = "base_point_time", nullable = false)
    @NotNull(message = "🚨baseDate 필수입니다.")
    private LocalDate baseDate; // 기준일자

    @Column(name = "trading_price", precision = 20, nullable = false)
    @NotNull(message = "🚨tradingPrice 필수입니다.")
    private BigDecimal tradingPrice; // 거래대금

    @Column(name = "market_total_amount", precision = 20, nullable = false)
    @NotNull(message = "🚨marketTotalAmount 필수입니다.")
    private BigDecimal marketTotalAmount; // 상장시가총액

    public void setUpdateIndexData(IndexDataUpdateRequest request) {

        if (null != request.marketPrice()) {
            BigDecimal marketPrice = new BigDecimal(String.valueOf(request.marketPrice()));
            if(!Objects.equals(this.marketPrice, marketPrice)) {
                this.marketPrice = marketPrice;
            }
        }
        if (null != request.closingPrice()) {
            BigDecimal closingPrice = new BigDecimal(String.valueOf(request.closingPrice()));
            if(!Objects.equals(this.closingPrice, closingPrice)) {
                this.closingPrice = closingPrice;
            }
        }
        if (null != request.highPrice()) {
            BigDecimal highPrice = new BigDecimal(String.valueOf(request.highPrice()));
            if(!Objects.equals(this.highPrice, highPrice)) {
                this.highPrice = highPrice;
            }
        }
        if (null != request.lowPrice()) {
            BigDecimal lowPrice = new BigDecimal(String.valueOf(request.lowPrice()));
            if(!Objects.equals(this.lowPrice, lowPrice)) {
                this.lowPrice = lowPrice;
            }
        }
        if (null != request.versus()) {
            BigDecimal versus = new BigDecimal(String.valueOf(request.versus()));
            if(!Objects.equals(this.versus, versus)) {
                this.versus = versus;
            }
        }
        if (null != request.fluctuationRate()) {
            BigDecimal fluctuationRate = new BigDecimal(String.valueOf(request.fluctuationRate()));
            if(!Objects.equals(this.fluctuationRate, fluctuationRate)) {
                this.fluctuationRate = fluctuationRate;
            }
        }
        if (null != request.tradingQuantity()) {
            BigDecimal tradingQuantity = new BigDecimal(String.valueOf(request.tradingQuantity()));
            if(!Objects.equals(this.tradingQuantity, tradingQuantity)) {
                this.tradingQuantity = tradingQuantity;
            }
        }
        if (null != request.tradingPrice()) {
            BigDecimal tradingPrice = new BigDecimal(String.valueOf(request.tradingPrice()));
            if (!Objects.equals(this.tradingPrice, tradingPrice)) {
                this.tradingPrice = tradingPrice;
            }
        }
        if (null != request.marketTotalAmount()) {
            BigDecimal marketTotalAmount = new BigDecimal(String.valueOf(request.marketTotalAmount()));
            if(!Objects.equals(this.marketTotalAmount, marketTotalAmount)) {
                this.marketTotalAmount = marketTotalAmount;
            }
        }
    }

    public static IndexData from(IndexInfo indexInfo, IndexDataCreateRequest request) {
        return new IndexData(
            indexInfo,
            request.marketPrice(),
            request.closingPrice(),
            request.highPrice(),
            request.lowPrice(),
            request.tradingQuantity(),
            request.versus(),
            request.fluctuationRate(),
            SourceType.USER, //??
            LocalDate.parse(request.baseDate()),
            request.tradingPrice(),
            request.marketTotalAmount()
        );
    }
}
