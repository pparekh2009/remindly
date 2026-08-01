# Remindly

Remindly is an MCP (Model Context Protocol) server for task tracking. It lets you manage a todo list directly from an AI client (Claude, Claude Code, etc.) using natural language, instead of a dedicated UI — the AI calls tools exposed by this server to create and list your tasks.

This is a personal learning project and my first MCP server. More tools (update, complete, delete tasks, reminders, etc.) are planned.

## How it works

Remindly is a Spring Boot application built on [Spring AI's MCP server starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html). It runs as a local process communicating over stdio (`spring.main.web-application-type=none`), and exposes Java methods as MCP tools via the `@McpTool` annotation. Tasks are persisted to a file-based H2 database through Spring Data JPA — no external database process required.

## Tools

| Tool | Description |
|---|---|
| `create_task` | Creates a new todo with an optional category, priority (`LOW`, `MEDIUM`, `HIGH`), and due date. |
| `get_all_tasks` | Retrieves all pending tasks. |

## Tech stack

- Java 17
- Spring Boot 4.1
- Spring AI MCP Server Starter (`spring-ai-starter-mcp-server`)
- Spring Data JPA
- H2 (file-based)

## Getting started

### Prerequisites

- Java 17+

### 1. Download

Grab the latest `remindly.jar` from the [Releases](../../releases) page.

### 2. Connect an MCP client

Point your MCP-compatible client (e.g. Claude Desktop, Claude Code) at the downloaded jar. Example config:

```json
{
  "mcpServers": {
    "remindly": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/remindly.jar"]
    }
  }
}
```

Once connected, you can talk to the AI naturally, e.g. "remind me to pay rent by the 5th, high priority" or "what's on my todo list?".

### Advanced: custom database location or credentials

By default, the database file is created as `data/remindly.mv.db` next to wherever the jar runs from, using default credentials. To customize this, place a `.env` file next to the jar:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:h2:file:./data/remindly` | JDBC URL of the H2 database file |
| `DB_USERNAME` | `sa` | Database username |
| `DB_PASSWORD` | *(empty)* | Database password |

## Building from source

Prefer to build it yourself instead of using a release jar? You'll need Java 17+ and the included `mvnw` wrapper:

```bash
./mvnw clean package
```

The runnable jar is produced at `target/remindly-0.0.1-SNAPSHOT.jar`. Point your MCP client at that path instead, same config shape as above.

## Project structure

```
src/main/java/com/priyanshparekh/remindly/
├── RemindlyApplication.java   # Spring Boot entry point
├── tool/TodoTools.java        # MCP tool definitions (@McpTool)
├── service/TodoService.java   # Business logic
├── repository/TodoRepository.java
├── entity/                    # JPA entities (Todo, Priority)
└── dto/                       # Request/response records
```

## License

MIT — see [LICENSE](LICENSE).
