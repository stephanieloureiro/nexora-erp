package com.nexora.erp.audit.controller;

import com.nexora.erp.audit.dto.AuditEventResponse;
import com.nexora.erp.audit.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-events")
public class AuditEventController {

    private final AuditService auditService;

    public AuditEventController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditEventResponse>> findAll(@RequestParam(required = false) String entityType,
                                                            @PageableDefault(size = 20) Pageable pageable) {
        if (entityType == null || entityType.isBlank()) {
            return ResponseEntity.ok(auditService.findAll(pageable));
        }

        return ResponseEntity.ok(auditService.findByEntityType(entityType, pageable));
    }
}
