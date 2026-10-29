package com.priyanshparekh.remindly.dto;

import com.priyanshparekh.remindly.entity.Todo;
import lombok.Builder;

@Builder
public record TodoResponse(
        Long id,
        String title,
        String dueAt,
        String category,
        String priority
) {

    public static TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDueAt() != null ? todo.getDueAt().toString() : null,
                todo.getCategory() != null ? todo.getCategory().getCategory() : null,
                todo.getPriority().name()
        );
    }

}
