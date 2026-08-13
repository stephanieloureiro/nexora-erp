# NEXORA ERP

API REST para gestao comercial de pequenas empresas, com controle de clientes, produtos, estoque e pedidos de venda.

O projeto foi desenvolvido como portfolio para demonstrar fundamentos valorizados em desenvolvimento back-end Java: orientacao a objetos, Spring Boot, APIs REST, JPA/Hibernate, banco relacional, Flyway, regras de negocio, testes automatizados e documentacao OpenAPI.

## Status do projeto

**Fase atual: Fase 6 - Diferenciais**

Status: MVP implementado, testado, documentado e com inicio dos diferenciais tecnicos.

Resultado atual dos testes:

```text
33 testes executados
0 falhas
```

## Funcionalidades

### Clientes

- Cadastro de clientes.
- Consulta por ID.
- Listagem com paginacao.
- Pesquisa por nome.
- Atualizacao cadastral.
- Inativacao sem exclusao fisica.
- Bloqueio de CPF/CNPJ duplicado.
- Validacao de e-mail.

### Produtos

- Cadastro de produtos.
- Consulta por ID.
- Listagem com paginacao.
- Pesquisa por nome ou SKU.
- Atualizacao cadastral.
- Inativacao sem exclusao fisica.
- Bloqueio de SKU duplicado.
- Identificacao de estoque baixo.
- Valores monetarios com `BigDecimal`.

### Movimentacoes de Estoque

- Registro de entrada.
- Registro de saida.
- Historico de movimentacoes.
- Atualizacao automatica do estoque.
- Bloqueio de quantidade invalida.
- Bloqueio de saida acima do estoque disponivel.
- Operacoes transacionais.

### Pedidos de Venda

- Criacao de pedido com itens.
- Validacao de cliente ativo.
- Validacao de produtos ativos.
- Calculo de subtotal e total no back-end.
- Confirmacao com baixa de estoque.
- Cancelamento com devolucao de estoque.
- Bloqueio de confirmacao sem estoque suficiente.
- Bloqueio de confirmacao de pedido cancelado.

## Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Bean Validation
- PostgreSQL
- Flyway
- Maven Wrapper
- JUnit 5
- Mockito
- H2 para testes
- Springdoc OpenAPI
- Spring Security
- JWT

## Arquitetura

O projeto usa arquitetura em camadas simples:

```text
controller -> service -> repository -> entity
              dto
              mapper
              exception
              config
```

Responsabilidades:

- `controller`: recebe requisicoes HTTP e retorna respostas.
- `service`: concentra regras de negocio.
- `repository`: acessa o banco de dados com Spring Data JPA.
- `entity`: representa tabelas e comportamentos centrais do dominio.
- `dto`: define dados de entrada e saida da API.
- `mapper`: converte entidades em respostas da API.
- `exception`: centraliza erros e respostas padronizadas.
- `config`: guarda configuracoes gerais da aplicacao.

## Estrutura principal

```text
src/main/java/com/nexora/erp
  common
    config
    exception
  customer
    controller
    dto
    entity
    mapper
    repository
    service
  product
    controller
    dto
    entity
    mapper
    repository
    service
  stock
    controller
    dto
    entity
    mapper
    repository
    service
  order
    controller
    dto
    entity
    mapper
    repository
    service
```

## Como executar

### Pre-requisitos

- Java 21
- PostgreSQL local ou via Docker, para o profile `dev`

O Maven global nao e necessario, porque o projeto usa Maven Wrapper.

### Executar rapidamente sem PostgreSQL

Para demonstrar a API sem instalar banco local, use o profile `local`.

Ele usa H2 em memoria e recria o banco sempre que a aplicacao inicia.

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Depois acesse:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

### Banco de desenvolvimento

Opcao recomendada para desenvolvimento: PostgreSQL via Docker Compose.

Essa opcao evita instalacao manual do PostgreSQL no Windows, facilita reproduzir o ambiente e deixa o projeto mais apresentavel para portfolio.

Subir o banco:

```bash
docker compose up -d
```

Parar o banco:

```bash
docker compose down
```

O profile `dev` espera um PostgreSQL local com:

```text
database: nexora_erp
username: nexora
password: nexora
host: localhost
port: 5432
```

Configuracao em:

```text
src/main/resources/application-dev.properties
```

### Executar testes

No Windows:

```bash
.\mvnw.cmd test
```

Os testes usam H2 em memoria com modo PostgreSQL.

### Compilar

```bash
.\mvnw.cmd compile
```

### Executar aplicacao

Com PostgreSQL local configurado:

```bash
.\mvnw.cmd spring-boot:run
```

## Documentacao da API

Com a aplicacao em execucao:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

Documentos complementares:

- [Exemplos de requisicoes](docs/api-examples.md)
- [Diagrama de entidades](docs/entity-diagram.md)
- [Guia de portfolio e entrevista](docs/portfolio-guide.md)
- [Roadmap da Fase 6](docs/phase-6-roadmap.md)
- [PostgreSQL com Docker](docs/docker-postgres.md)
- [Git, GitHub e CI](docs/git-github-ci.md)
- [Autenticacao e autorizacao](docs/authentication.md)

## Endpoints principais

### Clientes

```text
POST   /api/customers
GET    /api/customers/{id}
GET    /api/customers
GET    /api/customers/search?name=ana
PUT    /api/customers/{id}
PATCH  /api/customers/{id}/deactivate
```

### Produtos

```text
POST   /api/products
GET    /api/products/{id}
GET    /api/products
GET    /api/products/search?term=mouse
GET    /api/products/low-stock
PUT    /api/products/{id}
PATCH  /api/products/{id}/deactivate
```

### Estoque

```text
POST   /api/stock-movements
GET    /api/stock-movements
GET    /api/products/{productId}/stock-movements
```

### Pedidos

```text
POST   /api/sales-orders
GET    /api/sales-orders/{id}
GET    /api/sales-orders
PATCH  /api/sales-orders/{id}/confirm
PATCH  /api/sales-orders/{id}/cancel
```

### Autenticacao

```text
POST   /api/auth/login
```

Usuarios iniciais:

```text
admin@nexora.com / nexora123
operador@nexora.com / nexora123
```

## Regras de negocio importantes

- Entidades JPA nao sao expostas diretamente nos controllers.
- O sistema nao remove fisicamente clientes e produtos importantes; usa inativacao.
- CPF/CNPJ e SKU possuem restricao de unicidade.
- Pedido nao aceita cliente inativo.
- Pedido nao aceita produto inativo.
- Pedido deve possuir pelo menos um item.
- Quantidades devem ser positivas.
- Precos e totais sao calculados pelo back-end.
- Confirmar pedido baixa estoque.
- Cancelar pedido confirmado devolve estoque.
- Operacoes criticas usam transacao.

## Historico das fases

### Fase 1 - Planejamento

Status: aprovada.

Foram definidos o escopo do MVP, casos de uso, entidades, relacionamentos, endpoints, regras de negocio, estrutura de pastas, dependencias e ordem de implementacao.

### Fase 2 - Configuracao

Status: concluida.

Foi criado o projeto Spring Boot com Java 21, Maven Wrapper, profiles `dev` e `test`, Flyway, PostgreSQL Driver e H2 para testes.

### Fase 3 - Implementacao incremental

Status: concluida.

Modulos implementados:

- Clientes
- Produtos
- Movimentacoes de estoque
- Pedidos de venda

### Fase 4 - Testes

Status: concluida para o MVP.

Cenarios cobertos:

- Cadastro duplicado.
- Valores invalidos.
- Produto inativo.
- Cliente inativo.
- Saida acima do estoque.
- Confirmacao de pedido.
- Pedido sem estoque.
- Cancelamento com devolucao ao estoque.
- Calculo do valor total.

### Fase 5 - Documentacao e Portfolio

Status: concluida para o MVP.

Itens concluidos:

- OpenAPI configurado.
- Swagger UI disponivel.
- Exemplos de requisicao e resposta criados.
- Diagrama de entidades criado.
- Guia de portfolio e entrevista criado.
- README profissional atualizado.

### Fase 6 - Diferenciais

Status: concluida.

Decisao da etapa atual:

- O primeiro diferencial escolhido foi Docker Compose para PostgreSQL.
- O segundo diferencial escolhido foi CI com GitHub Actions.
- A decisao foi tomada por ser o melhor ganho imediato de qualidade para desenvolvimento, testes manuais e apresentacao do projeto.
- Spring Security, JWT, CI, auditoria, relatorios e multiempresa permanecem no roadmap, mas ainda nao foram implementados.

Itens concluidos nesta fase:

- Roadmap tecnico da Fase 6 criado.
- `docker-compose.yml` criado para PostgreSQL.
- Guia de uso do PostgreSQL com Docker criado.
- Workflow de CI criado com GitHub Actions.
- Guia de publicacao no GitHub e CI criado.
- README atualizado com a fase atual e instrucoes de continuidade.

### Fase 7 - Seguranca

Status: concluida.

Itens concluidos:

- Spring Security adicionado.
- Autenticacao por JWT implementada.
- Usuarios internos com perfis `ADMIN` e `EMPLOYEE` criados.
- Senhas protegidas com BCrypt.
- Endpoints de negocio protegidos.
- Swagger e login mantidos publicos para facilitar demonstracao.
- Testes de login, bloqueio sem token e bloqueio por permissao criados.
- Testes de clientes, produtos, estoque e pedidos atualizados para executar com usuario autenticado.
- Guia de autenticacao criado.

## Observacoes tecnicas

- O projeto foi preparado para Git local e GitHub Actions.
- O PostgreSQL de desenvolvimento pode ser criado via Docker Compose.
- O profile `local` permite demonstrar a aplicacao com H2 em memoria.
- O aviso conhecido do Mockito sobre carregamento dinamico de agente nao impacta os testes atuais.
- Neste ambiente, o Docker ainda precisa estar instalado para validar o `docker-compose.yml` em execucao.
- Auditoria, relatorios e front-end continuam como proximos diferenciais possiveis.
