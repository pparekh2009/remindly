package com.priyanshparekh.remindly.summary;

import com.priyanshparekh.remindly.dto.Period;
import com.priyanshparekh.remindly.dto.StatsResponse;
import com.priyanshparekh.remindly.dto.SummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SummaryTools {

    private final SummaryService summaryService;


    @McpTool(
            name = "get_tasks_stats",
            description = "Returns task statistics for a time period. completedCount, createdCount and dueInPeriodCount " +
                    "are counted within that period (periodDateRange shows the exact dates, inclusive). pendingCount, " +
                    "overdueCount, noDueDateCount, countByPriority and countByCategory describe the current state of " +
                    "pending todos and do not depend on the period. Overdue means due before today. Use this for " +
                    "questions about progress or workload, e.g. 'how productive was I this week?' or 'which category " +
                    "am I behind on?'. For a morning briefing or 'what's my day look like', use get_daily_summary instead."
    )
    public StatsResponse getTaskStats(@McpToolParam(description = "Time period for the period-based counts: TODAY, " +
            "THIS_WEEK (Monday to Sunday) or THIS_MONTH. Optional - defaults to THIS_WEEK.", required = false) Period period) {
        return summaryService.getTaskStats(period != null ? period : Period.THIS_WEEK);
    }

    @McpTool(
            name = "get_daily_summary",
            description = "Returns a snapshot of the user's day in one call: pending todos due today (sorted by due " +
                    "time, including ones whose time has already passed - compare dueAt with generatedAt), the number " +
                    "of overdue todos from previous days plus the most important few (topOverdueTodos), how many todos " +
                    "were completed today, and up to 5 high-priority todos with no due date. Use this when the user " +
                    "asks about their day, says good morning, or wants a briefing. Present it as a short, friendly " +
                    "briefing: lead with what's due today, then mention overdue items briefly; don't list every field. " +
                    "If overdueCount is larger than the todos shown, offer to show the rest with get_overdue_tasks."
    )
    public SummaryResponse getDailySummary() {
        return summaryService.getDailySummary();
    }

}
