package com.nexora.erp.report.dto;

public class StockSummaryResponse {

    private long activeProducts;
    private long lowStockProducts;
    private long totalStockQuantity;

    public StockSummaryResponse(long activeProducts, long lowStockProducts, long totalStockQuantity) {
        this.activeProducts = activeProducts;
        this.lowStockProducts = lowStockProducts;
        this.totalStockQuantity = totalStockQuantity;
    }

    public long getActiveProducts() {
        return activeProducts;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public long getTotalStockQuantity() {
        return totalStockQuantity;
    }
}
