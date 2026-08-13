package com.nexora.erp.product.entity;

import com.nexora.erp.common.exception.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, unique = true, length = 60)
    private String sku;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;

    @Column(nullable = false)
    private Boolean active;

    protected Product() {
    }

    public Product(String name, String description, String sku, BigDecimal price,
                   Integer stockQuantity, Integer minimumStock) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.minimumStock = minimumStock;
    }

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }

    public void update(String name, String description, String sku, BigDecimal price, Integer stockQuantity,
                       Integer minimumStock) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.minimumStock = minimumStock;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isLowStock() {
        return stockQuantity <= minimumStock;
    }

    public void increaseStock(Integer quantity) {
        stockQuantity += quantity;
    }

    public void decreaseStock(Integer quantity) {
        if (quantity > stockQuantity) {
            throw new BusinessRuleException("Nao ha estoque suficiente para realizar a saida.");
        }

        stockQuantity -= quantity;
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
}
