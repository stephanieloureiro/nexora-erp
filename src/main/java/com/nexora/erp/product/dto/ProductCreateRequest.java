package com.nexora.erp.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductCreateRequest {

    @NotBlank(message = "O nome do produto e obrigatorio.")
    @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
    private String name;

    @Size(max = 500, message = "A descricao deve ter no maximo 500 caracteres.")
    private String description;

    @NotBlank(message = "O SKU e obrigatorio.")
    @Size(max = 60, message = "O SKU deve ter no maximo 60 caracteres.")
    private String sku;

    @NotNull(message = "O preco e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O preco deve ser maior que zero.")
    private BigDecimal price;

    @NotNull(message = "A quantidade em estoque e obrigatoria.")
    @Min(value = 0, message = "A quantidade em estoque nao pode ser negativa.")
    private Integer stockQuantity;

    @NotNull(message = "O estoque minimo e obrigatorio.")
    @Min(value = 0, message = "O estoque minimo nao pode ser negativo.")
    private Integer minimumStock;

    public ProductCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }
}
