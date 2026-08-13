package com.nexora.erp.audit.dto;

import java.time.LocalDateTime;

public class AuditEventResponse {

    private Long id;
    private LocalDateTime occurredAt;
    private String username;
    private String action;
    private String entityType;
    private Long entityId;
    private String description;

    public AuditEventResponse(Long id, LocalDateTime occurredAt, String username, String action,
                              String entityType, Long entityId, String description) {
        this.id = id;
        this.occurredAt = occurredAt;
        this.username = username;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getDescription() {
        return description;
    }
}
