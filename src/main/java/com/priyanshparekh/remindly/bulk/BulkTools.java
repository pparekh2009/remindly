package com.priyanshparekh.remindly.bulk;

import com.priyanshparekh.remindly.dto.BulkActionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BulkTools {

    private final BulkService bulkService;

    @McpTool(
            name = "complete_tasks_by_category",
            description = "Mark ALL pending todos in a category as completed. This affects many todos at once and cannot be " +
            "undone. Before calling this, call get_tasks_by_category, show the user the todos that will be completed, " +
            "and get explicit confirmation. If unsure of the exact category name, call get_categories first. Returns " +
            "the number of todos completed and the list of them."
    )
    public BulkActionResult completeTasksByCategory(@McpToolParam(description = "Category name, e.g. 'work', 'home'") String category) {
        return bulkService.completeTasksByCategory(category);
    }

    @McpTool(
            name = "reschedule_overdue_tasks",
            description = "Move ALL overdue pending todos to a new due date. Each todo keeps its original time of day; " +
                    "only the date changes. This affects many todos at once. Before calling this, call " +
                    "get_overdue_tasks, show the user the todos that will be rescheduled and the new date, and get " +
                    "explicit confirmation. To give a single todo a different date, use update_todo instead (it can " +
                    "be combined with this, e.g. move everything to Friday and one todo to Monday). Returns the " +
                    "rescheduled todos with their new due dates."
    )
    public BulkActionResult rescheduleOverdueTasks(@McpToolParam(description = "New due date in ISO format YYYY-MM-DD. Convert relative dates like " +
            "'tomorrow' or 'next Monday' before calling. Must be today or later.") LocalDate newDate) {
        return bulkService.rescheduleOverdueTasks(newDate);
    }

}
