package com.priyanshparekh.remindly.tool;

import com.priyanshparekh.remindly.dto.AddTodoRequest;
import com.priyanshparekh.remindly.dto.TodoListResult;
import com.priyanshparekh.remindly.service.TodoService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

@Component
public class TodoTools {

    private final TodoService todoService;

    public TodoTools(TodoService todoService) {
        this.todoService = todoService;
    }

    @McpTool(
            name = "create_task",
            description = "Create a new Todo task."
    )
    public void addTodo(AddTodoRequest addTodoRequest) {
        todoService.addTodo(addTodoRequest);
    }


    @McpTool(
            name = "get_all_tasks",
            description = "Retrieves all the pending tasks"
    )
    public TodoListResult getAllTasks() {
        return todoService.getTodos();
    }
}
