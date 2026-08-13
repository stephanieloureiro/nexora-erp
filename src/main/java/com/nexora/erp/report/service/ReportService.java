package com.nexora.erp.report.service;

import com.nexora.erp.order.entity.SalesOrderStatus;
import com.nexora.erp.order.repository.SalesOrderRepository;
import com.nexora.erp.product.repository.ProductRepository;
import com.nexora.erp.report.dto.SalesSummaryResponse;
import com.nexora.erp.report.dto.StockSummaryResponse;
import com.nexora.erp.report.dto.TopProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {

    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;

    public ReportService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
    }

    public SalesSummaryResponse salesSummary() {
        return new SalesSummaryResponse(
                salesOrderRepository.countByStatus(SalesOrderStatus.CRIADO),
                salesOrderRepository.countByStatus(SalesOrderStatus.CONFIRMADO),
                salesOrderRepository.countByStatus(SalesOrderStatus.CANCELADO),
                salesOrderRepository.sumTotalAmountByStatus(SalesOrderStatus.CONFIRMADO)
        );
    }

    public List<TopProductResponse> topProducts() {
        return salesOrderRepository.findTopProductsByStatus(SalesOrderStatus.CONFIRMADO)
                .stream()
                .map(row -> new TopProductResponse(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).longValue(),
                        (BigDecimal) row[4]
                ))
                .toList();
    }

    public StockSummaryResponse stockSummary() {
        Long totalStockQuantity = productRepository.sumActiveStockQuantity();

        return new StockSummaryResponse(
                productRepository.countByActiveTrue(),
                productRepository.countActiveLowStock(),
                totalStockQuantity == null ? 0 : totalStockQuantity
        );
    }
}
