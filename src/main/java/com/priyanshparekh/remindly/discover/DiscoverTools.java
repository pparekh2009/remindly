package com.priyanshparekh.remindly.discover;

import com.priyanshparekh.remindly.dto.CategoriesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscoverTools {

    private final DiscoverService discoverService;

    @McpTool(
            name = "get_categories",
            description = "List all existing categories with the number of pending tasks in each (categories with no " +
                    "pending tasks are included with a count of 0). Call this before creating or updating a todo with a " +
                    "category, so you can reuse an existing category name instead of creating a near-duplicate (e.g. " +
                    "use 'work' rather than adding 'Work stuff'). Also use it when the user asks what categories they " +
                    "have, or before get_tasks_by_category if you are unsure of the exact category name."
    )
    public List<CategoriesResponse> getCategories() {
        return discoverService.getCategories();
    }

}
