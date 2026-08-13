# Relatorios gerenciais

A Fase 8 adiciona endpoints de leitura para demonstrar consultas agregadas e visao de negocio.

Todos os endpoints exigem usuario autenticado.

## Resumo de vendas

```text
GET /api/reports/sales-summary
```

Retorna:

- Quantidade de pedidos criados.
- Quantidade de pedidos confirmados.
- Quantidade de pedidos cancelados.
- Faturamento confirmado.

O faturamento considera apenas pedidos com status `CONFIRMADO`.

## Produtos mais vendidos

```text
GET /api/reports/top-products
```

Retorna produtos vendidos em pedidos confirmados, ordenados por maior quantidade vendida.

Campos principais:

- ID do produto.
- Nome.
- SKU.
- Quantidade vendida.
- Receita gerada.

## Resumo de estoque

```text
GET /api/reports/stock-summary
```

Retorna:

- Produtos ativos.
- Produtos ativos com estoque baixo.
- Quantidade total de itens em estoque.

## Valor para portfolio

Esses endpoints demonstram:

- Consultas agregadas com JPA.
- Separacao entre operacao e leitura gerencial.
- Uso de dados reais do dominio.
- Testes de integracao para regras de relatorio.
