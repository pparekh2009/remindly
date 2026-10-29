package com.priyanshparekh.remindly.summary;

import com.priyanshparekh.remindly.dto.*;
import com.priyanshparekh.remindly.entity.Priority;
import com.priyanshparekh.remindly.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TodoRepository todoRepository;

    public StatsResponse getTaskStats(Period period) {
        LocalDate today = LocalDate.now();

        LocalDateTime startTime;
        LocalDateTime endTime;

        switch (period) {
            case TODAY -> {
                startTime = today.atStartOfDay();
                endTime = today.atStartOfDay().plusDays(1);
            }
            case THIS_MONTH -> {
                startTime = today.withDayOfMonth(1).atStartOfDay();
                endTime = today.withDayOfMonth(1).plusMonths(1).atStartOfDay();
            }
            default -> {
                startTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                endTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(1).atStartOfDay();
            }
        }

        long completedTodosInPeriodCount = todoRepository.countByCompletedAtGreaterThanEqualAndCompletedAtLessThan(startTime, endTime);
        long createdTodosInPeriodCount = todoRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startTime, endTime);
        long dueTodosInPeriodCount = todoRepository.countByDueAtGreaterThanEqualAndDueAtLessThanAndCompletedAtIsNull(startTime, endTime);
        long pendingCount = todoRepository.countByCompletedAtIsNull();
        long overdueCount = todoRepository.countByDueAtLessThanAndCompletedAtIsNull(today.atStartOfDay());
        long noDueDateCount = todoRepository.countByDueAtIsNullAndCompletedAtIsNull();

        List<PriorityProjection> priorityWiseList = todoRepository.countByPriority();
        Map<String, Long> countByPriority = new LinkedHashMap<>();
        for (Priority p: Priority.values()) {
            countByPriority.put(p.name(), 0L);
        }
        priorityWiseList.forEach(priorityProjection -> countByPriority.put(priorityProjection.getPriority().name(), priorityProjection.getCount()));


        List<CategoryProjection> categoryWiseList = todoRepository.countByCategory();
        Map<String, Long> countByCategory = new LinkedHashMap<>();
        categoryWiseList.forEach(categoryProjection -> {
            countByCategory.put(categoryProjection.getCategory() != null ? categoryProjection.getCategory() : "Uncategorized", categoryProjection.getCount());
        });

        return new StatsResponse(
                period,
                startTime.toLocalDate() + " to " + endTime.toLocalDate().minusDays(1),
                completedTodosInPeriodCount,
                createdTodosInPeriodCount,
                dueTodosInPeriodCount,
                pendingCount,
                overdueCount,
                noDueDateCount,
                countByPriority,
                countByCategory
        );
    }

    public SummaryResponse getDailySummary() {
        List<TodoResponse> pendingTodosDueToday = todoRepository.findAllByDueAtGreaterThanEqualAndDueAtLessThanAndCompletedAtIsNull(LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay())
                .stream().map(TodoResponse::toResponse).toList();

        long overDueCount = todoRepository.countByDueAtLessThanAndCompletedAtIsNull(LocalDate.now().atStartOfDay());

        List<TodoResponse> topOverdueTodos = todoRepository.findAllByDueAtLessThanAndCompletedAtIsNullOrderByPriorityDescDueAtAsc(LocalDate.now().atStartOfDay(), Limit.of(5))
                .stream().map(TodoResponse::toResponse).toList();

        long todosCompletedTodayCount = todoRepository.countByCompletedAtGreaterThanEqualAndCompletedAtLessThan(LocalDate.now().atStartOfDay(), LocalDateTime.now());

        List<TodoResponse> highPriorityNoDueDateTodos = todoRepository.findAllByDueAtIsNullAndPriorityAndCompletedAtIsNull(Priority.HIGH, Limit.of(5))
                .stream().map(TodoResponse::toResponse).toList();

        return new SummaryResponse(
                LocalDate.now(),
                LocalDateTime.now(),
                pendingTodosDueToday,
                overDueCount,
                topOverdueTodos,
                todosCompletedTodayCount,
                highPriorityNoDueDateTodos
        );
    }

}
