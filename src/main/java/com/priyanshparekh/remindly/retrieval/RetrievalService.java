package com.priyanshparekh.remindly.retrieval;

import com.priyanshparekh.remindly.dto.TodoResponse;
import com.priyanshparekh.remindly.entity.Category;
import com.priyanshparekh.remindly.entity.Priority;
import com.priyanshparekh.remindly.repository.CategoryRepository;
import com.priyanshparekh.remindly.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RetrievalService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    public List<TodoResponse> getTasksByPriority(Priority priority) {
        return todoRepository.findAllByCompletedAtIsNullAndPriority(priority)
                .stream().map(TodoResponse::toResponse).toList();
    }

    public List<TodoResponse> getTasksByCategory(String categoryStr) {
        Category category = categoryRepository.findByCategoryIgnoreCase(categoryStr).orElse(null);

        if (category == null) {
            return new ArrayList<>();
        }

        return todoRepository.findAllByCategoryIdAndCompletedAtIsNull(category.getId())
                .stream().map(TodoResponse::toResponse).toList();
    }

    public List<TodoResponse> getTasksDueToday() {
        LocalDate today = LocalDate.now();
        return todoRepository.findAllByDueAtGreaterThanEqualAndDueAtLessThanAndCompletedAtIsNull(today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .stream().map(TodoResponse::toResponse).toList();
    }

    public List<TodoResponse> getOverdueTasks() {
        return todoRepository.findAllByDueAtLessThanAndCompletedAtIsNull(LocalDateTime.now())
                .stream().map(TodoResponse::toResponse).toList();
    }
}
