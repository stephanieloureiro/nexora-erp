package com.nexora.erp.report.dto;

import java.math.BigDecimal;

public class TopProductResponse {

    private Long productId;
    private String name;
    private String sku;
    private long quantitySold;
    private BigDecimal revenue;

    public TopProductResponse(Long productId, String name, String sku, long quantitySold, BigDecimal revenue) {
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public long getQuantitySold() {
        return quantitySold;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }
}
