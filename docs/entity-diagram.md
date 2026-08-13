# Diagrama de Entidades

```mermaid
erDiagram
    CUSTOMERS ||--o{ SALES_ORDERS : "realiza"
    SALES_ORDERS ||--|{ SALES_ORDER_ITEMS : "possui"
    PRODUCTS ||--o{ SALES_ORDER_ITEMS : "vendido em"
    PRODUCTS ||--o{ STOCK_MOVEMENTS : "movimenta"

    CUSTOMERS {
        bigint id PK
        varchar name
        varchar document UK
        varchar email
        varchar phone
        date registration_date
        boolean active
    }

    PRODUCTS {
        bigint id PK
        varchar name
        varchar description
        varchar sku UK
        numeric price
        integer stock_quantity
        integer minimum_stock
        boolean active
    }

    STOCK_MOVEMENTS {
        bigint id PK
        bigint product_id FK
        varchar type
        integer quantity
        varchar reason
        timestamp created_at
    }

    SALES_ORDERS {
        bigint id PK
        bigint customer_id FK
        timestamp created_at
        varchar status
        numeric total_amount
    }

    SALES_ORDER_ITEMS {
        bigint id PK
        bigint sales_order_id FK
        bigint product_id FK
        integer quantity
        numeric unit_price
        numeric subtotal
    }
```

## Relacionamentos

- Um cliente pode realizar varios pedidos.
- Um pedido pertence a um cliente.
- Um pedido possui um ou mais itens.
- Cada item referencia um produto.
- Um produto pode aparecer em varios itens de pedido.
- Um produto possui varias movimentacoes de estoque.
