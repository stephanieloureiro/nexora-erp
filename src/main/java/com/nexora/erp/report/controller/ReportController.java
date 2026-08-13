package com.nexora.erp.report.controller;

import com.nexora.erp.report.dto.SalesSummaryResponse;
import com.nexora.erp.report.dto.StockSummaryResponse;
import com.nexora.erp.report.dto.TopProductResponse;
import com.nexora.erp.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales-summary")
    public ResponseEntity<SalesSummaryResponse> salesSummary() {
        return ResponseEntity.ok(reportService.salesSummary());
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductResponse>> topProducts() {
        return ResponseEntity.ok(reportService.topProducts());
    }

    @GetMapping("/stock-summary")
    public ResponseEntity<StockSummaryResponse> stockSummary() {
        return ResponseEntity.ok(reportService.stockSummary());
    }
}
