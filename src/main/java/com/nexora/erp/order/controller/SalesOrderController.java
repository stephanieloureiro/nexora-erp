package com.nexora.erp.order.controller;

import com.nexora.erp.order.dto.SalesOrderCreateRequest;
import com.nexora.erp.order.dto.SalesOrderResponse;
import com.nexora.erp.order.service.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @PostMapping
    public ResponseEntity<SalesOrderResponse> create(@Valid @RequestBody SalesOrderCreateRequest request) {
        SalesOrderResponse response = salesOrderService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SalesOrderResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(salesOrderService.findAll(pageable));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<SalesOrderResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.confirm(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SalesOrderResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.cancel(id));
    }
}
