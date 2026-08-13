# Git, GitHub e CI

Este guia explica como publicar o Nexora ERP no GitHub e ativar a esteira de testes automatizados.

## O que ja foi configurado

- Repositorio Git local.
- Workflow em `.github/workflows/ci.yml`.
- Execucao automatica de testes com Java 21.
- Cache de dependencias Maven para acelerar o CI.

## Como validar localmente

Na raiz do projeto:

```bash
.\mvnw.cmd test
```

Esse e o mesmo objetivo do CI: garantir que o projeto compila e que os testes continuam passando.

## Como publicar no GitHub

1. Crie um repositorio vazio no GitHub chamado `nexora-erp`.
2. Copie a URL do repositorio.
3. Na raiz do projeto, configure o remoto:

```bash
git remote add origin URL_DO_REPOSITORIO
```

4. Envie o projeto:

```bash
git push -u origin main
```

Depois do push, o GitHub Actions deve executar o workflow `CI` automaticamente.

## Como explicar em entrevista

Este projeto usa CI para rodar testes automaticamente a cada push ou pull request para a branch principal.

Na pratica, isso ajuda a evitar que mudancas quebrem regras importantes do ERP, como cadastro duplicado, baixa de estoque e confirmacao de pedidos.

## Observacoes

- O CI usa H2 em memoria pelo profile de testes, entao nao precisa subir PostgreSQL no GitHub Actions.
- O PostgreSQL via Docker fica para execucao local com o profile `dev`.
- Quando o projeto tiver um front-end ou deploy, o CI pode ser expandido para empacotar a aplicacao e validar mais etapas.
