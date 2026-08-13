package com.nexora.erp.audit.service;

import com.nexora.erp.audit.dto.AuditEventResponse;
import com.nexora.erp.audit.entity.AuditEvent;
import com.nexora.erp.audit.mapper.AuditEventMapper;
import com.nexora.erp.audit.repository.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;
    private final AuditEventMapper auditEventMapper;

    public AuditService(AuditEventRepository auditEventRepository, AuditEventMapper auditEventMapper) {
        this.auditEventRepository = auditEventRepository;
        this.auditEventMapper = auditEventMapper;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(String action, String entityType, Long entityId, String description) {
        auditEventRepository.save(new AuditEvent(currentUsername(), action, entityType, entityId, description));
    }

    @Transactional(readOnly = true)
    public Page<AuditEventResponse> findAll(Pageable pageable) {
        return auditEventRepository.findAll(pageable)
                .map(auditEventMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditEventResponse> findByEntityType(String entityType, Pageable pageable) {
        return auditEventRepository.findByEntityTypeIgnoreCase(entityType, pageable)
                .map(auditEventMapper::toResponse);
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return "system";
        }

        return authentication.getName();
    }
}
