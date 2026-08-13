package com.nexora.erp.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SalesOrderCreateRequest {

    @NotNull(message = "O cliente e obrigatorio.")
    private Long customerId;

    @NotEmpty(message = "O pedido deve possuir pelo menos um item.")
    private List<@Valid SalesOrderItemCreateRequest> items;

    public SalesOrderCreateRequest() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<SalesOrderItemCreateRequest> getItems() {
        return items;
    }

    public void setItems(List<SalesOrderItemCreateRequest> items) {
        this.items = items;
    }
}
