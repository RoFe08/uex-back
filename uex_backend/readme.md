# 📘 Documentação Técnica – Backend UEX API

## 1. Visão Geral

A aplicação **UEX Backend** é uma API REST desenvolvida com **Spring Boot 3.3.0** e **Java 17**, responsável pelo gerenciamento de usuários, autenticação segura via JWT e controle de contatos com geolocalização integrada.

A arquitetura segue o padrão **MVC (Model-View-Controller)**, com separação clara de responsabilidades e tratamento centralizado de exceções.

---

## 2. Stack Tecnológica

### Linguagem e Plataforma

* Java 17
* Spring Boot 3.3.0

### Principais Dependências

| Biblioteca                          | Finalidade                    |
| ----------------------------------- | ----------------------------- |
| spring-boot-starter-web             | Criação de APIs REST          |
| spring-boot-starter-validation      | Validação com Bean Validation |
| spring-boot-starter-data-jpa        | ORM e persistência            |
| spring-boot-starter-security        | Autenticação e autorização    |
| PostgreSQL Driver                   | Banco de dados                |
| Lombok                              | Redução de boilerplate        |
| ModelMapper                         | Mapeamento DTO ↔ Entity       |
| jjwt-api / jjwt-impl / jjwt-jackson | Geração e validação de JWT    |

---

## 3. Arquitetura MVC

### Estrutura de pacotes

```
org.uex_back
│
├── controller
│   ├── AuthController
│   └── ContactController
│
├── service
│   ├── AuthService
│   ├── ContactService
│   ├── ContactGeolocationService
│
├── model
│   ├── User
│   ├── Contact
│
├── repository
│   ├── UserRepository
│   ├── ContactRepository
│
├── dto
│   ├── auth
│   │   ├── SignupRequest
│   │   ├── LoginRequest
│   │   ├── DeleteAccountRequest
│   │
│   ├── contact
│   │   ├── ContactRequest
│   │   └── ContactResponse
│
├── security
│   ├── JwtService
│   ├── JwtAuthenticationFilter
│   └── SecurityConfig
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── EmailAlreadyInUseException
│   ├── InvalidPasswordException
│   └── ErrorResponse
└── config
    └── ModelMapperConfig
```

---

## 4. Autenticação e Segurança

### Fluxo JWT

1. Usuário realiza login ou signup
2. Backend gera um JWT
3. Token é retornado ao Frontend
4. Frontend envia token no header:

```
Authorization: Bearer <token>
```

5. Filtro JwtAuthenticationFilter valida e injeta no SecurityContext

### Configuração de Rotas

```
Endpoint público:
- /api/auth/login
- /api/auth/signup

Endpoints protegidos:
- /api/contacts/**
- /api/auth/account (exclusão)
```

---

## 5. Tratamento de Exceções

Utiliza-se um handler global:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicado(...) {}

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleSenhaInvalida(...) {}
}
```

### Padrão de resposta de erro

```json
{
  "message": "Senha inválida."
}
```

---

## 6. Endpoints da API

### Autenticação

#### POST /api/auth/signup

Criação de novo usuário

Request:

```json
{
  "name": "Rodrigo Ferraz",
  "email": "teste@email.com",
  "password": "123456"
}
```

Response 201:

```json
{
  "token": "jwt...",
  "user": {
    "id": 1,
    "name": "Rodrigo",
    "email": "teste@email.com"
  }
}
```

Erro 409:

```json
{ "message": "O e-mail já está em uso." }
```

---

#### POST /api/auth/login

Request:

```json
{
  "email": "teste@email.com",
  "password": "123456"
}
```

Response:

```json
{
  "token": "jwt...",
  "user": { ... }
}
```

---

#### DELETE /api/auth/account

Exclusão da conta (requer senha)

Request:

```json
{
  "password": "123456"
}
```

Erro 409:

```json
{ "message": "Senha inválida." }
```

---

### Contatos

#### POST /api/contacts

Cria um contato

#### GET /api/contacts

Lista contatos do usuário

#### PUT /api/contacts/{id}

Edita um contato

#### DELETE /api/contacts/{id}

Remove um contato

---

## 7. Geolocalização

Utiliza padrão Strategy para consulta de coordenadas:

Interface:

```java
public interface GeolocalizacaoStrategy {
    LatitudeLongitudeDTO buscar(String endereco);
}
```

Implementação Google futura plugável sem impacto na arquitetura.

---

## 8. ModelMapper

Configuração customizada:

```java
@Bean
public ModelMapper modelMapper() {
  ModelMapper mapper = new ModelMapper();
  mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  return mapper;
}
```

---

## 9. Banco de Dados

Banco PostgreSQL com relacionamento:

* User 1:N Contact
* Cascade + OrphanRemoval para exclusão completa ao deletar usuário

---

## 10. Integração Frontend

Frontend Angular 17 consome a API utilizando:

* Interceptor HTTP
* Guard de autenticação
* ngx-toastr para exibição de mensagens

---

## 11. Melhorias futuras

* Swagger UI
* Versionamento de API
* Refresh Token
* Logs estruturados
* Auditoria de requisições

---

📌 Projeto desenvolvido para avaliação técnica Fullstack Java (Angular + Spring Boot)

Autor: Rodrigo Ferraz
Versão Java: 17
Backend: Spring Boot 3.3.0

---

## ✅ Implementação Oficial OpenAPI 3 + Swagger UI

Esta API está documentada utilizando o padrão **OpenAPI 3.0** com interface interativa via **Swagger UI**, permitindo visualizar, testar e validar todos os endpoints diretamente pelo navegador.

### 🔧 Dependência adicionada ao projeto

Adicione ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

Após iniciar a aplicação, a documentação estará disponível em:

```
http://localhost:8080/swagger-ui.html
```

E o JSON OpenAPI em:

```
http://localhost:8080/v3/api-docs
```

---

## 🧭 Configuração base do Swagger

Crie a classe:

```java
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "UEX API",
                version = "1.0.0",
                description = "API REST para gerenciamento de contatos com autenticação JWT"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
```

---

## 🔐 Autenticação JWT no Swagger

Todos os endpoints protegidos usam o header:

```
Authorization: Bearer {token}
```

No Swagger UI:

1. Clique em **Authorize**
2. Cole: `Bearer seu.token.jwt`
3. Todos os endpoints protegidos passarão a funcionar

---

## 📌 Exemplos de endpoints documentados com OpenAPI

### Login

```java
@PostMapping("/login")
@Operation(summary = "Autenticar usuário")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
})
public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
}
```

### Criar contato

```java
@PostMapping
@Operation(summary = "Cadastrar novo contato")
@SecurityRequirement(name = "bearerAuth")
public ResponseEntity<ContactResponse> create(@RequestBody @Valid ContactRequest request) {
    return ResponseEntity.ok(contactService.create(request));
}
```

---

## 📂 Endpoints documentados

### AUTENTICAÇÃO

| Método | Endpoint         | Descrição         |
| ------ | ---------------- | ----------------- |
| POST   | /api/auth/login  | Login do usuário  |
| POST   | /api/auth/signup | Cadastro          |
| DELETE | /api/auth/delete | Exclusão de conta |

### CONTATOS

| Método | Endpoint           | Protegido |
| ------ | ------------------ | --------- |
| GET    | /api/contacts      | ✅         |
| POST   | /api/contacts      | ✅         |
| PUT    | /api/contacts/{id} | ✅         |
| DELETE | /api/contacts/{id} | ✅         |
