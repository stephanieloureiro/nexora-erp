package com.nexora.erp.order.dto;

import com.nexora.erp.order.entity.SalesOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SalesOrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private SalesOrderStatus status;
    private List<SalesOrderItemResponse> items;
    private BigDecimal totalAmount;

    public SalesOrderResponse(Long id, Long customerId, String customerName, LocalDateTime createdAt,
                              SalesOrderStatus status, List<SalesOrderItemResponse> items,
                              BigDecimal totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SalesOrderStatus getStatus() {
        return status;
    }

    public List<SalesOrderItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
