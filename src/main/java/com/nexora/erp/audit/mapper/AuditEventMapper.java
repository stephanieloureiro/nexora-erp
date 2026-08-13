package com.nexora.erp.audit.mapper;

import com.nexora.erp.audit.dto.AuditEventResponse;
import com.nexora.erp.audit.entity.AuditEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditEventMapper {

    public AuditEventResponse toResponse(AuditEvent event) {
        return new AuditEventResponse(
                event.getId(),
                event.getOccurredAt(),
                event.getUsername(),
                event.getAction(),
                event.getEntityType(),
                event.getEntityId(),
                event.getDescription()
        );
    }
}
