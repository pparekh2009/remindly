package com.priyanshparekh.remindly.dto;

import org.springframework.ai.mcp.annotation.McpToolParam;

import java.time.LocalDateTime;

public record AddTodoRequest(
   @McpToolParam(description = "Title of the task.")
   String title,

   @McpToolParam(description = "Category to put this task under, e.g. 'home', 'work', etc. " +
           "Prefer an existing category name from get_categories over a new one. " +
           "Optional - omit if user does not mention it.", required = false)
   String category,

   @McpToolParam(description = "Priority level of the task. Must be one of these - LOW, MEDIUM, HIGH. " +
           "Optional - assign LOW priority if user does not mention it", required = false)
   String priority,

   @McpToolParam(description = "Due date in ISO format YYYY-MM-DD, e.g. 2026-07-08. " +
           "If the user mentions relative dates like 'tomorrow', or 'until Monday', convert it to this format before calling. " +
           "If no time is mentioned, assume midnight of the same day.", required = false)
   LocalDateTime dueAt
) {}
