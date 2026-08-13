package com.nexora.erp.product.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer minimumStock;
    private Boolean active;
    private Boolean lowStock;

    public ProductResponse(Long id, String name, String description, String sku, BigDecimal price,
                           Integer stockQuantity, Integer minimumStock, Boolean active, Boolean lowStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.minimumStock = minimumStock;
        this.active = active;
        this.lowStock = lowStock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getLowStock() {
        return lowStock;
    }
}
