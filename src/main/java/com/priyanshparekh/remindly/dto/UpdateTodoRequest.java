package com.priyanshparekh.remindly.dto;

import org.springframework.ai.mcp.annotation.McpToolParam;

import java.time.LocalDateTime;

public record UpdateTodoRequest(
        @McpToolParam(description = "The id of the todo to update. Required.")
        Long id,

        @McpToolParam(description = "New title for the todo. Omit/leave null to keep the current title.", required = false)
        String title,

        @McpToolParam(description = "New category for the todo. Prefer an existing category name from get_categories " +
                "over a new one. Omit/leave null to keep the current category.", required = false)
        String category,

        @McpToolParam(description = "New priority: LOW, MEDIUM, or HIGH. Omit/leave null to keep the current priority.", required = false)
        String priority,

        @McpToolParam(description = "New due date/time in ISO format. Omit/leave null to keep the current due date.", required = false)
        LocalDateTime dueAt
) { }
