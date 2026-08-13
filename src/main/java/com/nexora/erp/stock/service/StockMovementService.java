package com.nexora.erp.stock.service;

import com.nexora.erp.audit.service.AuditService;
import com.nexora.erp.common.exception.ResourceNotFoundException;
import com.nexora.erp.product.entity.Product;
import com.nexora.erp.product.repository.ProductRepository;
import com.nexora.erp.stock.dto.StockMovementCreateRequest;
import com.nexora.erp.stock.dto.StockMovementResponse;
import com.nexora.erp.stock.entity.StockMovement;
import com.nexora.erp.stock.entity.StockMovementType;
import com.nexora.erp.stock.mapper.StockMovementMapper;
import com.nexora.erp.stock.repository.StockMovementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final StockMovementMapper stockMovementMapper;
    private final AuditService auditService;

    public StockMovementService(StockMovementRepository stockMovementRepository,
                                ProductRepository productRepository,
                                StockMovementMapper stockMovementMapper,
                                AuditService auditService) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
        this.stockMovementMapper = stockMovementMapper;
        this.auditService = auditService;
    }

    @Transactional
    public StockMovementResponse create(StockMovementCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado."));

        if (request.getType() == StockMovementType.ENTRADA) {
            product.increaseStock(request.getQuantity());
        } else {
            product.decreaseStock(request.getQuantity());
        }

        StockMovement movement = new StockMovement(
                product,
                request.getType(),
                request.getQuantity(),
                request.getReason()
        );

        StockMovement savedMovement = stockMovementRepository.save(movement);
        auditService.record("STOCK_MOVEMENT_CREATED", "StockMovement", savedMovement.getId(),
                "Movimentacao de estoque registrada para o produto " + product.getId() + ".");
        return stockMovementMapper.toResponse(savedMovement);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findAll(Pageable pageable) {
        return stockMovementRepository.findAll(pageable)
                .map(stockMovementMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByProductId(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Produto nao encontrado.");
        }

        return stockMovementRepository.findByProductId(productId, pageable)
                .map(stockMovementMapper::toResponse);
    }
}
