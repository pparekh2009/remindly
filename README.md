# Remindly

Remindly is an MCP (Model Context Protocol) server for task tracking. Manage your todos through natural language from an AI client (Claude, Claude Code, etc.) instead of a dedicated UI.

Personal learning project — my first MCP server. Planned next: more reporting tools — e.g. listing completed todos (all-time or within a date range), overdue todos, todos filtered by category, and todos due within a date range.

## Features

- Create, find, update, complete, and delete todos by natural language
- Priority levels: `LOW`, `MEDIUM`, `HIGH`
- Optional category and due date per todo
- Case-insensitive partial-text search (`find_todos`)
- Mutations are id-only — the AI must confirm a match with you before acting on it
- No external database process — everything persists to a local H2 file

## Tools

| Tool | Description |
|---|---|
| `create_todo` | Creates a todo — title, optional category, priority (default `LOW`), due date. |
| `get_all_pending_todos` | Retrieves all pending (not yet completed) todos. |
| `find_todos` | Case-insensitive partial-text search on titles, returns candidates with ids. |
| `complete_todo` | Marks a todo completed, by id. |
| `update_todo` | Updates one or more fields of a todo, by id. Omitted fields are left as-is. |
| `delete_todo` | Permanently deletes a todo, by id. Irreversible. |

## Tech stack

- Java 17
- Spring Boot 4.1
- Spring AI MCP Server Starter
- Spring Data JPA
- H2 (file-based)

## Getting started

**Prerequisites:** Java 17+

1. Grab the latest `remindly.jar` from the [Releases](../../releases) page.
2. Point your MCP client (Claude Desktop, Claude Code, etc.) at the jar:

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

3. Talk to the AI naturally — e.g. "remind me to pay rent by the 5th, high priority" or "what's on my todo list?".

## Custom database location or credentials

- Default location: `~/.remindly/data/remindly.mv.db` — universal across all projects and sessions.
- To override, place a `.env` file next to the jar:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:h2:file:${user.home}/.remindly/data/remindly` | JDBC URL of the H2 database file |
| `DB_USERNAME` | `sa` | Database username |
| `DB_PASSWORD` | *(empty)* | Database password |

Want an isolated per-project todo list instead? Set `DB_URL` in that project's MCP config:

```json
{
  "mcpServers": {
    "remindly": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/remindly.jar"],
      "env": { "DB_URL": "jdbc:h2:file:./data/remindly" }
    }
  }
}
```

## Building from source

You'll need Java 17+ and the included `mvnw` wrapper:

```bash
./mvnw clean package
```

- Runnable jar: `target/remindly-0.0.1-SNAPSHOT.jar`
- Point your MCP client at that path, same config shape as above.
- If your client keeps a long-lived reference to the jar path (e.g. Claude Code's `mcp add`), rebuilding overwrites the same file — a session already connected won't pick up the new build until it reconnects/restarts.

## License

MIT — see [LICENSE](LICENSE).
