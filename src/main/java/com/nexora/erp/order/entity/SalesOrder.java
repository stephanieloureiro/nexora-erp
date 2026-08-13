package com.nexora.erp.order.entity;

import com.nexora.erp.common.exception.BusinessRuleException;
import com.nexora.erp.customer.entity.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SalesOrderStatus status;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesOrderItem> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    protected SalesOrder() {
    }

    public SalesOrder(Customer customer) {
        this.customer = customer;
        this.status = SalesOrderStatus.CRIADO;
        this.totalAmount = BigDecimal.ZERO;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addItem(SalesOrderItem item) {
        item.setSalesOrder(this);
        items.add(item);
        recalculateTotal();
    }

    public void confirm() {
        if (status == SalesOrderStatus.CANCELADO) {
            throw new BusinessRuleException("Pedido cancelado nao pode ser confirmado.");
        }
        if (status == SalesOrderStatus.CONFIRMADO) {
            throw new BusinessRuleException("Pedido ja esta confirmado.");
        }

        status = SalesOrderStatus.CONFIRMADO;
    }

    public void cancel() {
        if (status == SalesOrderStatus.CANCELADO) {
            throw new BusinessRuleException("Pedido ja esta cancelado.");
        }

        status = SalesOrderStatus.CANCELADO;
    }

    private void recalculateTotal() {
        totalAmount = items.stream()
                .map(SalesOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SalesOrderStatus getStatus() {
        return status;
    }

    public List<SalesOrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
