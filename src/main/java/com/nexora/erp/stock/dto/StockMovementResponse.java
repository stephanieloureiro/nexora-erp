package com.nexora.erp.stock.dto;

import com.nexora.erp.stock.entity.StockMovementType;

import java.time.LocalDateTime;

public class StockMovementResponse {

    private Long id;
    private Long productId;
    private String productName;
    private StockMovementType type;
    private Integer quantity;
    private String reason;
    private LocalDateTime createdAt;

    public StockMovementResponse(Long id, Long productId, String productName, StockMovementType type,
                                 Integer quantity, String reason, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public StockMovementType getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
