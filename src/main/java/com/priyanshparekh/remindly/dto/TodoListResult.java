package com.priyanshparekh.remindly.dto;

import java.util.List;

public record TodoListResult(
        List<TodoResult> todoList
) { }
