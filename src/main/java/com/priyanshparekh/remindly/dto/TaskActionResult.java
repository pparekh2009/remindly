package com.priyanshparekh.remindly.dto;

import com.priyanshparekh.remindly.entity.Todo;

import java.util.List;

public record TaskActionResult(
        String status,
        String message,
        TodoResponse todoResponse,
        List<TodoResponse> candidateList
) {

    public static TaskActionResult success(Todo todo, String message) {
        return new TaskActionResult(
                "success",
                message,
                TodoResponse.from(todo),
                null
        );
    }

    public static TaskActionResult notFound(long id) {
        return new TaskActionResult(
                "not_found",
                "Todo with " + id + " not found",
                null,
                null
        );
    }

}
