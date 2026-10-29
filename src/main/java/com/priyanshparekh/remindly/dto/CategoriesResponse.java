package com.priyanshparekh.remindly.dto;

public record CategoriesResponse(
        String category,
        int pendingTasks
) { }
