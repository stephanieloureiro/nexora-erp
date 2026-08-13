# Guia da interface web

Este guia registra a primeira interface visual do Nexora ERP.

## Objetivo

A interface foi criada para permitir uso basico do sistema sem depender apenas do Swagger.

Ela e servida pelo proprio Spring Boot em:

```text
http://localhost:8080/
```

## Como executar

Para demonstracao sem PostgreSQL:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Depois abra:

```text
http://localhost:8080/
```

## Login de demonstracao

```text
admin@nexora.com / nexora123
```

## O que a interface permite fazer

- Login com JWT.
- Visualizar indicadores de vendas e estoque.
- Cadastrar clientes.
- Cadastrar produtos.
- Registrar entradas e saidas de estoque.
- Criar pedidos de venda com um item.
- Confirmar pedidos criados.
- Consultar eventos de auditoria.
- Abrir o Swagger em uma nova aba.

## Arquivos criados

```text
src/main/resources/static/index.html
src/main/resources/static/app.css
src/main/resources/static/app.js
```

## Observacoes de arquitetura

- A tela e estatica e fica dentro do proprio back-end.
- O token JWT e guardado no `localStorage` do navegador.
- Os endpoints da API continuam protegidos.
- Apenas a tela inicial e os assets publicos foram liberados sem autenticacao.
- Esta abordagem e simples e adequada para portfolio e demonstracao inicial.

## Evolucoes futuras

- Criar seletores de cliente e produto em vez de campos de ID.
- Permitir pedidos com multiplos itens pela interface.
- Adicionar edicao e inativacao pela tela.
- Separar front-end em projeto proprio quando houver necessidade de maior escala.
