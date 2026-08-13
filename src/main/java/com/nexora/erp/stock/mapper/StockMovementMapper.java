package com.nexora.erp.stock.mapper;

import com.nexora.erp.stock.dto.StockMovementResponse;
import com.nexora.erp.stock.entity.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovementResponse toResponse(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getProduct().getName(),
                movement.getType(),
                movement.getQuantity(),
                movement.getReason(),
                movement.getCreatedAt()
        );
    }
}
