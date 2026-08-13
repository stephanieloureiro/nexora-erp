# Guia de Portfolio e Entrevista

## Descricao curta para GitHub

API REST em Java e Spring Boot para gestao comercial, com clientes, produtos, estoque, pedidos de venda, regras de negocio, JPA, Flyway, testes automatizados e documentacao OpenAPI.

## Descricao para LinkedIn

Desenvolvi o NEXORA ERP, uma API REST de gestao comercial para pequenas empresas, usando Java 21, Spring Boot, JPA/Hibernate, Flyway, PostgreSQL, Bean Validation e testes automatizados. O projeto contempla clientes, produtos, movimentacoes de estoque e pedidos de venda, com regras de negocio como controle de estoque, bloqueio de duplicidades, calculo de totais no back-end, confirmacao e cancelamento de pedidos com transacoes.

## Sequencia sugerida de commits

```text
chore: create spring boot project structure
chore: configure profiles, flyway and test database
feat: implement customer module
test: add customer unit and integration tests
feat: implement product module
test: add product business rule tests
feat: implement stock movement module
test: cover stock movement rules
feat: implement sales order module
test: cover sales order confirmation and cancellation
docs: add openapi configuration and api examples
docs: update readme and portfolio guide
```

## Como explicar decisoes tecnicas

### Por que usar DTOs?

Usei DTOs para separar o que a API recebe e devolve da estrutura interna das entidades JPA. Isso evita expor detalhes do banco nos controllers e deixa a API mais controlada.

### Por que usar BigDecimal?

Valores monetarios precisam de precisao. `double` e `float` podem gerar erros de arredondamento, entao usei `BigDecimal` para preco, subtotal e valor total.

### Por que usar Flyway?

Flyway versiona o banco de dados. Cada mudanca estrutural fica registrada em uma migration, o que ajuda a reproduzir o ambiente e entender a evolucao do projeto.

### Por que usar transacao em estoque e pedidos?

Operacoes como confirmar pedido precisam atualizar estoque e registrar historico. Se uma parte falhar, a transacao desfaz tudo, preservando a consistencia dos dados.

### Por que nao usar Lombok no inicio?

Nao usei Lombok para deixar construtores, getters e metodos visiveis. Isso facilita o aprendizado e ajuda a explicar a estrutura das classes em entrevista.

## Perguntas de entrevista

1. Por que voce separou entity e DTO?
2. Como o sistema impede cliente ou produto duplicado?
3. Por que o total do pedido nao vem da API?
4. O que acontece se faltar estoque ao confirmar um pedido?
5. Como o cancelamento devolve estoque?
6. Onde estao as regras de negocio?
7. Qual a responsabilidade do controller?
8. Por que voce usou Flyway?
9. Qual a diferenca entre teste unitario e teste de integracao neste projeto?
10. O que voce melhoraria depois do MVP?

## Melhorias futuras recomendadas

- Autenticacao com Spring Security e JWT.
- Perfis ADMIN e EMPLOYEE.
- Docker para PostgreSQL e aplicacao.
- CI com GitHub Actions.
- Relatorios de vendas e estoque.
- Auditoria de alteracoes importantes.
