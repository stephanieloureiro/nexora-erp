# Auditoria

A Fase 9 adiciona rastreabilidade para operacoes importantes do ERP.

## O que e registrado

Eventos de auditoria sao gravados em `audit_events` com:

- Data e hora.
- Usuario autenticado.
- Acao executada.
- Tipo da entidade.
- ID da entidade.
- Descricao curta.

## Endpoint

```text
GET /api/audit-events
```

Somente usuarios `ADMIN` podem consultar auditoria.

Filtro opcional:

```text
GET /api/audit-events?entityType=Customer
```

## Exemplos de acoes

```text
CUSTOMER_CREATED
CUSTOMER_UPDATED
CUSTOMER_DEACTIVATED
PRODUCT_CREATED
PRODUCT_UPDATED
PRODUCT_DEACTIVATED
STOCK_MOVEMENT_CREATED
ORDER_CREATED
ORDER_CONFIRMED
ORDER_CANCELED
```

## Valor para portfolio

Auditoria demonstra preocupacao com sistemas empresariais reais:

- Rastreabilidade.
- Responsabilidade por alteracoes.
- Consulta administrativa.
- Integracao com autenticacao e autorizacao.
