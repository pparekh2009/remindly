# Remindly

Remindly is an MCP (Model Context Protocol) server for task tracking. Manage your todos through natural language from an AI client (Claude, Claude Code, etc.) instead of a dedicated UI.

## Features

- **Just talk to it** — "remind me to pay rent by the 5th" or "what's due this week?". No forms, no extra app to open.
- **Priorities and due dates** — mark tasks high, medium or low, and give them a deadline. "Tomorrow" and "next Friday" work too.
- **Organized by category** — group tasks into work, home, or anything you like. Remindly reuses the categories you already have, so your list stays tidy.
- **Find anything** — describe a task in your own words and Remindly finds it.
- **Asks before it acts** — before completing, changing or deleting a task, it shows you exactly which one it means.
- **Clean up in bulk** — finish a whole category at once, or move everything overdue to tomorrow, keeping each task's time.
- **Morning briefing** — ask "what does my day look like?" to see what's due today, what's overdue, and what you've already done.
- **See your progress** — how much you got done today, this week or this month, and which categories need attention.
- **Works across all your AI apps** — use it from Claude Desktop and several Claude Code sessions at the same time; it's always the same list.
- **Private by default** — your tasks stay in a file on your own computer. No account, no cloud, nothing extra to install.

## Tools

| Tool | Description |
|---|---|
| `create_todo` | Creates a todo — title, optional category, priority (default `LOW`), due date. |
| `get_all_pending_todos` | Retrieves all pending (not yet completed) todos. |
| `find_todos` | Case-insensitive partial-text search on titles, returns candidates with ids. |
| `complete_todo` | Marks a todo completed, by id. |
| `update_todo` | Updates one or more fields of a todo, by id. Omitted fields are left as-is. |
| `delete_todo` | Permanently deletes a todo, by id. Irreversible. |

### Retrieval

| Tool | Description |
|---|---|
| `get_tasks_by_priority` | Retrieves all pending todos with the given priority (`LOW`, `MEDIUM`, `HIGH`). |
| `get_tasks_by_category` | Retrieves all pending todos in a category (case-insensitive, exact name). Empty if the category doesn't exist. |
| `get_tasks_due_today` | Retrieves all pending todos due today, including ones whose time today has already passed. |
| `get_overdue_tasks` | Retrieves all pending todos whose due date/time has passed. |

### Discovery

| Tool | Description |
|---|---|
| `get_categories` | Lists all categories with their pending-todo count (categories with nothing pending show `0`). |

### Bulk actions

| Tool | Description |
|---|---|
| `complete_tasks_by_category` | Marks every pending todo in a category as completed. Affects many todos at once and can't be undone — the AI confirms with you first. |
| `reschedule_overdue_tasks` | Moves all overdue todos to a new date (`YYYY-MM-DD`, today or later). Each todo keeps its original time of day. The AI confirms with you first. |

### Summary & stats

| Tool | Description |
|---|---|
| `get_tasks_stats` | Stats for `TODAY`, `THIS_WEEK` (Monday–Sunday, default) or `THIS_MONTH`: completed, created and due counts in the period, plus current pending, overdue (due before today), no-due-date counts and pending counts by priority and by category. |
| `get_daily_summary` | Today's pending todos, overdue count with the top 5 overdue todos, completed-today count, and high-priority todos with no due date — in one call, for a morning briefing. |

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

More things to try:

- "What does my day look like?"
- "How productive was I this week?"
- "What categories do I have?"
- "Move all my overdue stuff to tomorrow."
- "I finished everything in work, mark it all done."

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

### Using multiple clients at once

Each MCP client (every Claude Code session, Claude Desktop, etc.) starts its own Remindly process. The default URL ends with `;AUTO_SERVER=TRUE`, which lets these processes share one database file: the first process to open it serves it to the others over localhost, and another takes over if it exits.

If you set your own `DB_URL`, add `;AUTO_SERVER=TRUE` to it as well — e.g. `jdbc:h2:file:./data/remindly;AUTO_SERVER=TRUE`. Without it, only one client can use the database at a time and the others fail with `Database may be already in use`.

## Logs

Logs are written to `~/.remindly/logs/remindly.log`. Nothing is logged to the console, because the MCP stdio transport uses stdout for protocol messages.

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
