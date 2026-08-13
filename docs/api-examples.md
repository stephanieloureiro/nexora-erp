# Exemplos de Requisicoes da API

Base URL local:

```text
http://localhost:8080
```

Documentacao OpenAPI:

```text
http://localhost:8080/v3/api-docs
http://localhost:8080/swagger-ui.html
```

## Clientes

### Cadastrar cliente

```http
POST /api/customers
Content-Type: application/json
```

```json
{
  "name": "Ana Silva",
  "document": "123.456.789-00",
  "email": "ana@email.com",
  "phone": "11999999999"
}
```

Resposta esperada:

```json
{
  "id": 1,
  "name": "Ana Silva",
  "document": "12345678900",
  "email": "ana@email.com",
  "phone": "11999999999",
  "registrationDate": "2026-08-12",
  "active": true
}
```

### Pesquisar clientes por nome

```http
GET /api/customers/search?name=ana
```

## Produtos

### Cadastrar produto

```http
POST /api/products
Content-Type: application/json
```

```json
{
  "name": "Mouse sem fio",
  "description": "Mouse ergonomico para escritorio.",
  "sku": "mouse-001",
  "price": 89.90,
  "stockQuantity": 10,
  "minimumStock": 3
}
```

Resposta esperada:

```json
{
  "id": 1,
  "name": "Mouse sem fio",
  "description": "Mouse ergonomico para escritorio.",
  "sku": "MOUSE-001",
  "price": 89.90,
  "stockQuantity": 10,
  "minimumStock": 3,
  "active": true,
  "lowStock": false
}
```

### Listar produtos com estoque baixo

```http
GET /api/products/low-stock
```

## Movimentacoes de Estoque

### Registrar entrada

```http
POST /api/stock-movements
Content-Type: application/json
```

```json
{
  "productId": 1,
  "type": "ENTRADA",
  "quantity": 5,
  "reason": "Compra de mercadorias"
}
```

### Registrar saida

```http
POST /api/stock-movements
Content-Type: application/json
```

```json
{
  "productId": 1,
  "type": "SAIDA",
  "quantity": 2,
  "reason": "Ajuste de estoque"
}
```

## Pedidos de Venda

### Criar pedido

```http
POST /api/sales-orders
Content-Type: application/json
```

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 3
    }
  ]
}
```

Resposta esperada:

```json
{
  "id": 1,
  "customerId": 1,
  "customerName": "Ana Silva",
  "createdAt": "2026-08-12T20:00:00",
  "status": "CRIADO",
  "items": [
    {
      "productId": 1,
      "productName": "Mouse sem fio",
      "quantity": 3,
      "unitPrice": 89.90,
      "subtotal": 269.70
    }
  ],
  "totalAmount": 269.70
}
```

### Confirmar pedido

```http
PATCH /api/sales-orders/1/confirm
```

### Cancelar pedido

```http
PATCH /api/sales-orders/1/cancel
```

## Exemplos de Erro

### Documento duplicado

```json
{
  "timestamp": "2026-08-12T20:00:00-03:00",
  "status": 409,
  "error": "CONFLICT",
  "message": "Ja existe um cliente cadastrado com este CPF ou CNPJ.",
  "path": "/api/customers",
  "details": []
}
```

### Estoque insuficiente

```json
{
  "timestamp": "2026-08-12T20:00:00-03:00",
  "status": 422,
  "error": "UNPROCESSABLE_CONTENT",
  "message": "Nao ha estoque suficiente para realizar a saida.",
  "path": "/api/sales-orders/1/confirm",
  "details": []
}
```
