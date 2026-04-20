<div align="center">

# ✂️ BarberDev API

**Sistema de agendamento para barbearias — Backend REST em Java 21 + Spring Boot 3**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

</div>

---

## 📋 Sobre o Projeto

O **BarberDev API** é a camada de backend de um sistema fullstack de agendamento para barbearias. A API fornece endpoints RESTful para gerenciamento completo de **clientes** e **agendamentos**, com foco em **robustez**, **qualidade de código** e **boas práticas de engenharia**.

> **Frontend:** [barber-dev-ui](https://github.com/alissonjcjk/Barber-shop-appointments-ui) — Angular 18 com design system premium.

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas com **CQRS** (Command Query Responsibility Segregation) aplicado na camada de serviço:

```
┌─────────────────────────────────────────┐
│             REST Controllers            │  ← Validação de entrada (@Valid)
├─────────────────────────────────────────┤
│      IClientService / IScheduleService  │  ← Operações de escrita (@Transactional)
│  IClientQueryService / IScheduleQuery.. │  ← Operações de leitura (readOnly = true)
├─────────────────────────────────────────┤
│         MapStruct Mappers               │  ← Conversão Request ↔ Entity ↔ Response
├─────────────────────────────────────────┤
│    Spring Data JPA Repositories         │  ← Acesso a dados (query derivada)
├─────────────────────────────────────────┤
│         PostgreSQL + Flyway             │  ← Banco de dados + migrações versionadas
└─────────────────────────────────────────┘
```

### Decisões de Design

| Decisão | Justificativa |
|---|---|
| **CQRS na camada de serviço** | Separa responsabilidades de leitura e escrita; queries rodam com `readOnly = true` — Hibernate desabilita dirty-checking e o pool de conexões é liberado mais cedo |
| **Interfaces para todas as dependências** | Favorece testabilidade e inversão de dependência (DIP) |
| **MapStruct em vez de mapeamento manual** | Mapeamento com zero boilerplate e segurança de tipos em tempo de compilação |
| **Flyway para migrations** | Controle de versão do schema; `ddl-auto: validate` garante que a entidade e o banco estejam sempre sincronizados |
| **RFC 7807 para respostas de erro** | Formato padronizado e autodescritivo para todos os erros da API |
| **`open-in-view: false`** | Previne o problema N+1 e sessões Hibernate abertas durante a serialização JSON |
| **CORS via `ConfigurationProperties`** | Origens permitidas configuráveis por profile Spring — restrito em produção sem recompilação |
| **Timezone configurável no controller** | Limites do mês no endpoint `/schedules/{year}/{month}` calculados no fuso local (BRT), não em UTC — evita agendamentos noturnos aparecerem no mês errado |

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 (Records, Pattern Matching, Text Blocks) |
| Framework | Spring Boot 3.4.2 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 17 |
| Migrations | Flyway |
| Mapeamento | MapStruct 1.6.3 |
| Validação | Jakarta Bean Validation |
| Build | Gradle 8 (Kotlin DSL) |
| Container | Docker + Docker Compose |
| Utilitários | Lombok, Log4j2 |

---

## 📡 Endpoints da API

### Clientes — `POST /clients`

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `POST` | `/clients` | Cadastrar novo cliente | `201 Created` |
| `PUT` | `/clients/{id}` | Atualizar dados do cliente | `200 OK` |
| `DELETE` | `/clients/{id}` | Remover cliente | `204 No Content` |
| `GET` | `/clients/{id}` | Buscar cliente por ID | `200 OK` |
| `GET` | `/clients` | Listar todos os clientes (ordenado por nome) | `200 OK` |

### Agendamentos — `POST /schedules`

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `POST` | `/schedules` | Criar agendamento | `201 Created` |
| `DELETE` | `/schedules/{id}` | Cancelar agendamento | `204 No Content` |
| `GET` | `/schedules/{year}/{month}` | Listar agendamentos do mês | `200 OK` |

### Respostas de Erro (RFC 7807)

Todos os erros seguem o formato padronizado:

```json
{
  "status": 409,
  "timestamp": "2025-04-30T22:10:00Z",
  "message": "Já existe um agendamento no horário solicitado. Escolha outro horário.",
  "fields": []
}
```

| Código | Cenário |
|--------|---------|
| `404` | Recurso não encontrado |
| `409` | E-mail/telefone já em uso; conflito de horário no agendamento |
| `422` | Campos inválidos (detalhes por campo no array `fields`) |
| `500` | Erro interno inesperado |

---

## 🔒 Regras de Negócio

- **Unicidade de clientes**: e-mail e telefone são únicos no banco (restrição de DB + verificação na camada de serviço)
- **Conflito de agendamento**: detecta **sobreposição real de intervalos** — não apenas horários idênticos
  - Lógica: `existingStart < newEnd AND existingEnd > newStart`
- **Validação de datas futuras**: `startAt` e `endAt` são validados com `@Future`
- **Telefone único**: formato `11 dígitos` armazenado como `bpchar(11)` no PostgreSQL
- **Schema validado**: `ddl-auto: validate` garante integridade contínua entre entidades e banco

---

## 🚀 Executando o Projeto

### Pré-requisitos

- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Java 21](https://adoptium.net/) (apenas para desenvolvimento local sem Docker)

### Com Docker Compose (recomendado)

```bash
# 1. Clone o repositório
git clone https://github.com/alissonjcjk/Barber-shop-appointments-api.git
cd Barber-shop-appointments-api

# 2. Crie a rede Docker compartilhada (apenas na primeira vez)
docker network create barber-dev-net

# 3. Suba o ambiente completo (API + PostgreSQL)
docker compose up --build
```

A API estará disponível em: **`http://localhost:8080`**

### Localmente (sem Docker)

```bash
# Configure as variáveis de ambiente do banco de dados
export DB_URL=jdbc:postgresql://localhost:5432/barber-dev-api
export DB_USER=barber-dev-api
export DB_PASSWORD=barber-dev-api
export SPRING_PROFILES_ACTIVE=dev

# Execute a aplicação
./gradlew bootRun
```

### Gerando um novo arquivo de migration Flyway

```bash
./gradlew generateFlywayMigrationFile -PmigrationName=nome_da_migration
```

---

## 🗄️ Banco de Dados

### Diagrama de Entidades

```
┌──────────────────────┐         ┌──────────────────────────┐
│       CLIENTS        │         │        SCHEDULES         │
├──────────────────────┤         ├──────────────────────────┤
│ id       BIGSERIAL PK│◄──┐     │ id        BIGSERIAL PK   │
│ name     VARCHAR(150)│   └─────│ client_id BIGINT FK      │
│ email    VARCHAR(150)│         │ start_at  TIMESTAMPTZ    │
│ phone    BPCHAR(11)  │         │ end_at    TIMESTAMPTZ    │
└──────────────────────┘         └──────────────────────────┘
  UK: email, phone                 UK: (start_at, end_at)
                                   IDX: client_id
                                   IDX: (start_at, end_at)
```

### Migrations Flyway

| Arquivo | Descrição |
|---------|-----------|
| `V20250201154932__create_table_clients.sql` | Criação da tabela `CLIENTS` |
| `V20250201155041__create_table_schedules.sql` | Criação da tabela `SCHEDULES` |
| `V20250201160000__alter_schedules_timestamps_to_timestamptz.sql` | Converte `TIMESTAMP` → `TIMESTAMPTZ` |
| `V20250201160001__add_indexes_schedules.sql` | Índices de performance em `SCHEDULES` |

---

## ⚙️ Configuração

### Variáveis de Ambiente

| Variável | Descrição | Obrigatória |
|----------|-----------|-------------|
| `DB_URL` | URL JDBC do PostgreSQL | ✅ |
| `DB_USER` | Usuário do banco | ✅ |
| `DB_PASSWORD` | Senha do banco | ✅ |
| `SPRING_PROFILES_ACTIVE` | Profile ativo (`dev` ou `prod`) | ✅ |

### Propriedades Configuráveis (`application.yml`)

```yaml
# Timezone da barbearia (para cálculo correto dos limites do mês)
app:
  timezone: America/Sao_Paulo

# CORS — origens permitidas (restringir em produção)
cors:
  allowed-origins: "https://barberdev.com.br"
```

---

## 🧪 Estrutura do Projeto

```
src/main/java/br/com/barberdev/api/
│
├── config/                   # Configurações Spring (CORS, Properties)
│   ├── CorsConfig.java
│   └── CorsProperties.java
│
├── controller/               # Controllers REST
│   ├── ClientController.java
│   ├── ScheduleController.java
│   ├── request/              # DTOs de entrada (records + @Valid)
│   └── response/             # DTOs de saída (records)
│
├── entity/                   # Entidades JPA
│   ├── ClientEntity.java
│   └── ScheduleEntity.java
│
├── exception/                # Exceções de domínio
├── exceptionhandler/         # Handler global (RFC 7807)
│
├── mapper/                   # MapStruct (Request ↔ Entity ↔ Response)
│   ├── IClientMapper.java
│   └── IScheduleMapper.java
│
├── repository/               # Spring Data JPA
│   ├── IClientRepository.java
│   └── IScheduleRepository.java
│
└── service/
    ├── IClientService.java       # Command: escrita de clientes
    ├── IScheduleService.java     # Command: escrita de agendamentos
    ├── impl/
    │   ├── ClientService.java
    │   └── ScheduleService.java
    └── query/
        ├── IClientQueryService.java    # Query: leitura de clientes
        ├── IScheduleQueryService.java  # Query: leitura de agendamentos
        └── impl/
            ├── ClientQueryService.java   # @Transactional(readOnly = true)
            └── ScheduleQueryService.java # @Transactional(readOnly = true)
```

---

## 🐳 Docker

O projeto usa **multi-stage build** para otimizar a imagem de produção:

```dockerfile
# Stage 1: Build (JDK completo)
FROM eclipse-temurin:21-jdk AS builder
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime (apenas JRE — imagem ~60% menor)
FROM eclipse-temurin:21-jre
COPY --from=builder *.jar app.jar
EXPOSE 8080 5005  # 5005 para debug remoto
```

---

## 👤 Autor

Desenvolvido por **Alisson da Silva Bernadino** como parte do projeto **BarberDev** — sistema fullstack de agendamento para barbearias.

---

<div align="center">

**BarberDev API** · Java 21 · Spring Boot 3 · PostgreSQL 17

</div>
