package com.nexora.erp.order.repository;

import com.nexora.erp.order.entity.SalesOrder;
import com.nexora.erp.order.entity.SalesOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    long countByStatus(SalesOrderStatus status);

    @Query("""
            select coalesce(sum(order.totalAmount), 0)
            from SalesOrder order
            where order.status = :status
            """)
    BigDecimal sumTotalAmountByStatus(SalesOrderStatus status);

    @Query("""
            select item.product.id, item.product.name, item.product.sku,
                   sum(item.quantity), sum(item.subtotal)
            from SalesOrderItem item
            where item.salesOrder.status = :status
            group by item.product.id, item.product.name, item.product.sku
            order by sum(item.quantity) desc
            """)
    List<Object[]> findTopProductsByStatus(SalesOrderStatus status);
}
