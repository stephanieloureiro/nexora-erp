# Fase 6 - Diferenciais

Esta fase avalia melhorias que podem ser adicionadas depois do MVP. A regra principal e incluir apenas o que resolve um problema real e pode ser explicado com clareza em uma entrevista de estagio.

## Analise dos diferenciais

### Spring Security e JWT

Problema que resolve:

- Protege a API para que apenas usuarios autenticados possam acessar recursos.
- Permite separar operacoes por tipo de usuario.

Conhecimento demonstrado:

- Autenticacao.
- Autorizacao.
- Filtros HTTP.
- Controle de acesso em APIs REST.

Dificuldade:

- Media.
- Exige entender fluxo de login, geracao de token, validacao de token e seguranca de endpoints.

Vale para estagio?

- Sim, mas somente depois do MVP estar bem documentado.
- E um bom diferencial, mas pode tomar bastante tempo se for feito com profundidade.

### Perfis ADMIN e EMPLOYEE

Problema que resolve:

- Diferencia quem pode administrar cadastros e quem pode apenas operar vendas/estoque.

Conhecimento demonstrado:

- Regras de autorizacao.
- Modelagem de papeis.
- Protecao de endpoints por permissao.

Dificuldade:

- Media.
- Normalmente depende de Spring Security.

Vale para estagio?

- Sim, se vier junto com uma autenticacao simples e bem explicada.

### Docker

Problema que resolve:

- Facilita subir PostgreSQL e aplicacao em qualquer maquina.
- Reduz problema de "funciona na minha maquina".

Conhecimento demonstrado:

- Ambientes reproduziveis.
- Configuracao de servicos.
- Noções de deploy local.

Dificuldade:

- Baixa a media.
- Para este projeto, pode comecar apenas com PostgreSQL via Docker Compose.

Vale para estagio?

- Sim. E provavelmente o diferencial com melhor custo-beneficio neste momento.

### CI com GitHub Actions

Problema que resolve:

- Executa testes automaticamente a cada push ou pull request.

Conhecimento demonstrado:

- Automacao.
- Qualidade continua.
- Fluxo profissional com GitHub.

Dificuldade:

- Baixa a media.
- Depende do projeto estar em um repositorio Git.

Vale para estagio?

- Sim, especialmente depois que o Git estiver organizado.

### Relatorios

Problema que resolve:

- Ajuda a visualizar vendas, estoque baixo e historico operacional.

Conhecimento demonstrado:

- Consultas.
- Agregacoes.
- Endpoints de leitura.

Dificuldade:

- Media.

Vale para estagio?

- Sim, mas depois de autenticacao ou Docker. Relatorios aumentam escopo de negocio.

### Contas a pagar e receber

Problema que resolve:

- Amplia o ERP para controle financeiro.

Conhecimento demonstrado:

- Modelagem de dominio.
- Regras financeiras.

Dificuldade:

- Alta para o momento atual.

Vale para estagio?

- Nao agora. Pode deixar o projeto grande demais.

### Auditoria

Problema que resolve:

- Registra quem alterou dados importantes e quando.

Conhecimento demonstrado:

- Rastreabilidade.
- Boas praticas em sistemas empresariais.

Dificuldade:

- Media.

Vale para estagio?

- Sim, mas faz mais sentido depois de autenticacao.

### Arquitetura SaaS multitenant

Problema que resolve:

- Permite separar dados de empresas diferentes na mesma aplicacao.

Conhecimento demonstrado:

- Arquitetura avancada.
- Isolamento de dados.

Dificuldade:

- Alta.

Vale para estagio?

- Nao agora. E um tema avancado e pode confundir a explicacao do MVP.

### Mensageria

Problema que resolve:

- Desacopla processos, como envio de notificacoes ou integracoes.

Conhecimento demonstrado:

- Processamento assíncrono.
- Eventos.
- Integracao entre sistemas.

Dificuldade:

- Alta para este momento.

Vale para estagio?

- Nao agora. O MVP nao tem necessidade real de fila.

## Recomendacao

O melhor proximo diferencial e **Docker para PostgreSQL**, porque:

- Resolve um problema real do projeto: ainda nao ha PostgreSQL local configurado.
- Facilita demonstrar a API com banco persistente.
- E simples de explicar.
- Nao muda as regras de negocio.
- Prepara o projeto para GitHub e execucao por outras pessoas.

Depois de Docker, a ordem recomendada e:

1. Inicializar Git e organizar commits.
2. CI com GitHub Actions.
3. Spring Security com JWT.
4. Perfis ADMIN e EMPLOYEE.
5. Auditoria.
6. Relatorios.
