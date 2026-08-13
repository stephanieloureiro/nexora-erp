package com.nexora.erp.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SalesOrderItemCreateRequest {

    @NotNull(message = "O produto e obrigatorio.")
    private Long productId;

    @NotNull(message = "A quantidade e obrigatoria.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantity;

    public SalesOrderItemCreateRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
