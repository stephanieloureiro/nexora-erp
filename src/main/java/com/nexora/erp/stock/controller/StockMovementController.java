package com.nexora.erp.stock.controller;

import com.nexora.erp.stock.dto.StockMovementCreateRequest;
import com.nexora.erp.stock.dto.StockMovementResponse;
import com.nexora.erp.stock.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping("/stock-movements")
    public ResponseEntity<StockMovementResponse> create(@Valid @RequestBody StockMovementCreateRequest request) {
        StockMovementResponse response = stockMovementService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/stock-movements")
    public ResponseEntity<Page<StockMovementResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(stockMovementService.findAll(pageable));
    }

    @GetMapping("/products/{productId}/stock-movements")
    public ResponseEntity<Page<StockMovementResponse>> findByProductId(@PathVariable Long productId,
                                                                       @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(stockMovementService.findByProductId(productId, pageable));
    }
}
