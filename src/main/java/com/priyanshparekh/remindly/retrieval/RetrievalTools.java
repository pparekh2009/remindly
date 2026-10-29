package com.priyanshparekh.remindly.retrieval;

import com.priyanshparekh.remindly.dto.TodoResponse;
import com.priyanshparekh.remindly.entity.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RetrievalTools {

    private final RetrievalService retrievalService;

    @McpTool(
            name = "get_tasks_by_priority",
            description = "Retrieve all the pending tasks with the given priority."
    )
    public List<TodoResponse> getTasksByPriority(Priority priority) {
        return retrievalService.getTasksByPriority(priority);
    }

    @McpTool(
            name = "get_tasks_by_category",
            description = "Retrieve all the pending tasks in the given category (case-insensitive, exact name match). " +
                    "Returns an empty list if the category does not exist or has no pending tasks. If you are unsure " +
                    "of the exact category name, call get_categories first."
    )
    public List<TodoResponse> getTasksByCategory(@McpToolParam(description = "Category name, e.g. 'work', 'home'") String category) {
        return retrievalService.getTasksByCategory(category);
    }

    @McpTool(
            name = "get_tasks_due_today",
            description = "Retrieve all the pending tasks due today, including ones whose due time earlier today has already passed."
    )
    public List<TodoResponse> getTasksDueToday() {
        return retrievalService.getTasksDueToday();
    }

    @McpTool(
            name = "get_overdue_tasks",
            description = "Retrieve all the pending tasks whose due date has passed."
    )
    public List<TodoResponse> getOverdueTasks() {
        return retrievalService.getOverdueTasks();
    }
}
