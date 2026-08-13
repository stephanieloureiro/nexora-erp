# PostgreSQL com Docker

Este arquivo explica como subir apenas o PostgreSQL com Docker Compose.

A aplicacao Java continua rodando localmente pela IDE ou pelo Maven Wrapper. Isso deixa o projeto mais simples para estudar e explicar.

## Pre-requisitos

- Docker Desktop instalado.
- Docker Compose disponivel.

## Subir PostgreSQL

Na raiz do projeto:

```bash
docker compose up -d
```

Verificar se o container ficou saudavel:

```bash
docker compose ps
```

O banco sera criado com:

```text
database: nexora_erp
username: nexora
password: nexora
host: localhost
port: 5432
```

Esses dados batem com o profile `dev` em:

```text
src/main/resources/application-dev.properties
```

## Rodar a aplicacao com PostgreSQL

Depois que o banco estiver de pe:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Ao iniciar, o Flyway cria as tabelas automaticamente.

## Parar PostgreSQL

```bash
docker compose down
```

## Apagar os dados do banco

Use apenas quando quiser zerar o banco local:

```bash
docker compose down -v
```

O `-v` remove o volume onde os dados ficam salvos.

## Observacao para Windows

Se a porta `5432` ja estiver em uso por outro PostgreSQL instalado na maquina, pare o servico local ou altere a porta publicada no `docker-compose.yml`.
