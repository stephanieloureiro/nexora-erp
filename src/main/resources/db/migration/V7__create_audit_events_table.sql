create table audit_events (
    id bigserial primary key,
    occurred_at timestamp not null,
    username varchar(160) not null,
    action varchar(80) not null,
    entity_type varchar(80) not null,
    entity_id bigint,
    description varchar(500)
);

create index idx_audit_events_occurred_at on audit_events (occurred_at);
create index idx_audit_events_entity on audit_events (entity_type, entity_id);
