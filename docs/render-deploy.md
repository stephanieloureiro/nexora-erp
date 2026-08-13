# Deploy no Render

Este guia registra a preparacao do Nexora ERP para ficar online como aplicacao Spring Boot com PostgreSQL.

## Importante

O GitHub Pages hospeda apenas a pagina estatica de portfolio.

O sistema completo precisa de um servidor Java e um banco PostgreSQL. Para isso, o projeto foi preparado para deploy no Render usando o arquivo:

```text
render.yaml
```

## O que o Blueprint cria

- Um Web Service Docker chamado `nexora-erp`, executando Java 21.
- Um banco PostgreSQL chamado `nexora-erp-db`.
- Variaveis de ambiente para conexao com o banco.
- Variaveis separadas para host, porta, banco, usuario e senha do PostgreSQL.
- Uma chave JWT gerada automaticamente.
- Profile `prod` do Spring Boot.

## Arquivos de producao

```text
render.yaml
Dockerfile
src/main/resources/application-prod.properties
```

## Como publicar pelo painel do Render

1. Acesse `https://dashboard.render.com`.
2. Conecte sua conta GitHub.
3. Escolha a opcao de criar um Blueprint.
4. Selecione o repositorio `stephanieloureiro/nexora-erp`.
5. Confirme a criacao dos recursos definidos no `render.yaml`.
6. Aguarde o build e o deploy.

Ao final, o Render mostrara uma URL publica parecida com:

```text
https://nexora-erp.onrender.com
```

## Como validar

Depois do deploy, abra:

```text
https://URL_DO_RENDER/
https://URL_DO_RENDER/swagger-ui.html
```

Login de demonstracao:

```text
admin@nexora.com / nexora123
```

## Observacoes

- O banco de producao usa PostgreSQL.
- As tabelas sao criadas pelo Flyway no primeiro deploy.
- O H2 continua disponivel apenas para `local` e `test`.
- A variavel `NEXORA_JWT_SECRET` nao deve ser colocada no codigo.
- O plano escolhido no `render.yaml` pode precisar ser ajustado no painel caso a disponibilidade do plano gratuito mude.
- Bancos PostgreSQL gratuitos no Render expiram apos 30 dias; para manter os dados, use um plano pago.
- O Render Blueprint atual nao aceita `runtime: java`; por isso o deploy usa `runtime: docker`.
- O build Docker usa imagem Maven com Eclipse Temurin 21 e a execucao usa Eclipse Temurin 21 JRE.
- A URL JDBC e montada no Spring com `DATABASE_HOST`, `DATABASE_PORT` e `DATABASE_NAME`; usuario e senha ficam em variaveis separadas para evitar formato invalido no driver PostgreSQL.
