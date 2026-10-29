package com.priyanshparekh.remindly.dto;

import java.util.Map;

public record StatsResponse(
        Period period,
        String periodDateRange,
        long completedCount,
        long createdCount,
        long dueInPeriodCount,
        long pendingCount,
        long overdueCount,
        long noDueDateCount,
        Map<String, Long> countByPriority,
        Map<String, Long> countByCategory
) {
}
