package com.nexora.erp.stock.dto;

import com.nexora.erp.stock.entity.StockMovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StockMovementCreateRequest {

    @NotNull(message = "O produto e obrigatorio.")
    private Long productId;

    @NotNull(message = "O tipo de movimentacao e obrigatorio.")
    private StockMovementType type;

    @NotNull(message = "A quantidade e obrigatoria.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantity;

    @NotBlank(message = "O motivo e obrigatorio.")
    @Size(max = 255, message = "O motivo deve ter no maximo 255 caracteres.")
    private String reason;

    public StockMovementCreateRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public StockMovementType getType() {
        return type;
    }

    public void setType(StockMovementType type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
