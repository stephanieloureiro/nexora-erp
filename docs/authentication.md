# Autenticacao e autorizacao

O Nexora ERP usa autenticao por JWT para proteger os endpoints de negocio.

## Usuarios iniciais

Ao iniciar a aplicacao com banco vazio, dois usuarios sao criados automaticamente:

```text
ADMIN
email: admin@nexora.com
senha: nexora123

EMPLOYEE
email: operador@nexora.com
senha: nexora123
```

As senhas sao salvas no banco com BCrypt.

## Fazer login

Endpoint:

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

Resposta:

```json
{
  "tokenType": "Bearer",
  "accessToken": "TOKEN_GERADO",
  "expiresInSeconds": 3600
}
```

## Usar o token

Nas chamadas protegidas, envie o header:

```text
Authorization: Bearer TOKEN_GERADO
```

## Regras de acesso

- `/api/auth/login`, Swagger UI e OpenAPI ficam publicos.
- Demais endpoints exigem autenticacao.
- `ADMIN` pode cadastrar, atualizar e inativar produtos.
- `ADMIN` pode inativar clientes.
- `EMPLOYEE` pode operar clientes, estoque e pedidos.

## Configuracao do segredo JWT

Em desenvolvimento, existe um segredo padrao:

```text
nexora-erp-local-development-secret-key-1234567890
```

Em ambiente real, configure a variavel:

```text
NEXORA_JWT_SECRET
```

## Testes

Os testes cobrem:

- Login com credenciais validas.
- Login com senha invalida.
- Bloqueio de endpoint sem autenticacao.
- Bloqueio de operacao sem permissao.
- Regressao dos fluxos de clientes, produtos, estoque e pedidos com usuario autenticado.
