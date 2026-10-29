package com.priyanshparekh.remindly.dto;

import java.util.List;

public record BulkActionResult(
        String status,
        String message,
        List<TodoResponse> todoResponseList
) {

    public static BulkActionResult success(List<TodoResponse> todoResponseList, String message) {
        return new BulkActionResult("success", message, todoResponseList);
    }

    public static BulkActionResult error(List<TodoResponse> todoResponseList, String message) {
        return new BulkActionResult("error", message, todoResponseList);
    }

}
