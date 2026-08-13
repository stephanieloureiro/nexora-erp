package com.nexora.erp.report.dto;

import java.math.BigDecimal;

public class SalesSummaryResponse {

    private long createdOrders;
    private long confirmedOrders;
    private long canceledOrders;
    private BigDecimal confirmedRevenue;

    public SalesSummaryResponse(long createdOrders, long confirmedOrders, long canceledOrders,
                                BigDecimal confirmedRevenue) {
        this.createdOrders = createdOrders;
        this.confirmedOrders = confirmedOrders;
        this.canceledOrders = canceledOrders;
        this.confirmedRevenue = confirmedRevenue;
    }

    public long getCreatedOrders() {
        return createdOrders;
    }

    public long getConfirmedOrders() {
        return confirmedOrders;
    }

    public long getCanceledOrders() {
        return canceledOrders;
    }

    public BigDecimal getConfirmedRevenue() {
        return confirmedRevenue;
    }
}
