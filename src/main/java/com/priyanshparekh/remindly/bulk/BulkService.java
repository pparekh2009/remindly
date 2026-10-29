package com.priyanshparekh.remindly.bulk;

import com.priyanshparekh.remindly.dto.BulkActionResult;
import com.priyanshparekh.remindly.dto.TodoResponse;
import com.priyanshparekh.remindly.entity.Category;
import com.priyanshparekh.remindly.entity.Todo;
import com.priyanshparekh.remindly.repository.CategoryRepository;
import com.priyanshparekh.remindly.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    public BulkActionResult completeTasksByCategory(String categoryStr) {
        Category category = categoryRepository.findByCategoryIgnoreCase(categoryStr).orElse(null);
        List<Todo> pendingTasks = todoRepository.findAllByCategoryIdAndCompletedAtIsNull(category.getId());
        LocalDateTime now = LocalDateTime.now();
        pendingTasks.forEach(task -> task.setCompletedAt(now));
        List<Todo> updatedTodos = todoRepository.saveAll(pendingTasks);
        return BulkActionResult.success(
                updatedTodos.stream().map(TodoResponse::toResponse).toList(),
                "Completed " + pendingTasks.size() + " todos in '" + categoryStr + "'"
        );
    }

    public BulkActionResult rescheduleOverdueTasks(LocalDate newDate) {
        if (newDate.isBefore(LocalDate.now())) {
            return BulkActionResult.error(
                    null,
                    "New date cannot be before today's date"
            );
        }
        List<Todo> pendingTasks = todoRepository.findAllByDueAtLessThanAndCompletedAtIsNull(LocalDateTime.now());
        pendingTasks.forEach(task -> task.setDueAt(newDate.atTime(task.getDueAt().toLocalTime())));
        List<Todo> updatedTodos = todoRepository.saveAll(pendingTasks);
        return BulkActionResult.success(
                updatedTodos.stream().map(TodoResponse::toResponse).toList(),
                "Todos Updated"
        );
    }

}
