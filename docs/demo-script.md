# Roteiro de demonstracao

Este roteiro mostra uma ordem simples para apresentar o Nexora ERP pelo Swagger UI.

## 1. Iniciar a aplicacao

Sem PostgreSQL:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Acesse:

```text
http://localhost:8080/swagger-ui.html
```

## 2. Fazer login

Use:

```text
POST /api/auth/login
```

Corpo:

```json
{
  "email": "admin@nexora.com",
  "password": "nexora123"
}
```

Copie o `accessToken` retornado.

## 3. Autorizar no Swagger

No Swagger UI:

1. Clique em `Authorize`.
2. Informe:

```text
Bearer TOKEN_GERADO
```

3. Confirme.

## 4. Criar dados principais

Crie um cliente:

```text
POST /api/customers
```

Crie um produto:

```text
POST /api/products
```

Registre uma entrada de estoque, se desejar:

```text
POST /api/stock-movements
```

## 5. Criar e confirmar pedido

Crie um pedido:

```text
POST /api/sales-orders
```

Confirme o pedido:

```text
PATCH /api/sales-orders/{id}/confirm
```

Isso baixa estoque automaticamente.

## 6. Ver relatorios

Consulte:

```text
GET /api/reports/sales-summary
GET /api/reports/top-products
GET /api/reports/stock-summary
```

## 7. Ver auditoria

Com usuario admin:

```text
GET /api/audit-events
```

Esse endpoint mostra os eventos gerados pelas operacoes anteriores.

## Narrativa para portfolio

Uma boa explicacao curta:

```text
O Nexora ERP e uma API REST de gestao comercial com clientes, produtos, estoque, pedidos, autenticacao JWT, relatorios gerenciais, auditoria, migrations com Flyway, testes automatizados e CI preparado para GitHub Actions.
```
