package com.priyanshparekh.remindly.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SummaryResponse(
        LocalDate todayDate,
        LocalDateTime generatedAt,
        List<TodoResponse> dueToday,
        Long overdueCount,
        List<TodoResponse> topOverdueTodos,
        Long completedTodayCount,
        List<TodoResponse> highPriorityNoDueDate
) {
}
