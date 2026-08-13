package com.nexora.erp.order.mapper;

import com.nexora.erp.order.dto.SalesOrderItemResponse;
import com.nexora.erp.order.dto.SalesOrderResponse;
import com.nexora.erp.order.entity.SalesOrder;
import com.nexora.erp.order.entity.SalesOrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalesOrderMapper {

    public SalesOrderResponse toResponse(SalesOrder order) {
        List<SalesOrderItemResponse> items = order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return new SalesOrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCreatedAt(),
                order.getStatus(),
                items,
                order.getTotalAmount()
        );
    }

    private SalesOrderItemResponse toItemResponse(SalesOrderItem item) {
        return new SalesOrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
