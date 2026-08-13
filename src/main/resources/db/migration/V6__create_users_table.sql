create table users (
    id bigserial primary key,
    name varchar(120) not null,
    email varchar(160) not null unique,
    password varchar(255) not null,
    role varchar(30) not null,
    active boolean not null,
    created_at timestamp not null
);

create index idx_users_email on users (email);
