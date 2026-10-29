package com.priyanshparekh.remindly.core;

import com.priyanshparekh.remindly.dto.AddTodoRequest;
import com.priyanshparekh.remindly.dto.TaskActionResult;
import com.priyanshparekh.remindly.dto.TodoResponse;
import com.priyanshparekh.remindly.dto.UpdateTodoRequest;
import com.priyanshparekh.remindly.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoreTools {

    private final CoreService coreService;

    @McpTool(
            name = "create_todo",
            description = "Create a new Todo task. If the user mentions a category, call get_categories first and reuse " +
                    "an existing category name when one clearly matches what the user meant; only pass a new category " +
                    "name when none fits. Returns the created todo with its id."
    )
    public TaskActionResult addTodo(AddTodoRequest addTodoRequest) {
        Todo todo = coreService.addTodo(addTodoRequest);
        return TaskActionResult.success(todo, "Todo created");
    }


    @McpTool(
            name = "get_all_pending_todos",
            description = "Retrieves all the pending todos"
    )
    public List<TodoResponse> getAllPendingTodos() {
        return coreService.getAllPendingTodos();
    }

    @McpTool(
            name = "delete_todo",
            description = "Permanently delete a todo by its id. This is irreversible. If you don't have a confident id " +
                    "for the todo the user described, call find_todos first. Even when find_todos returns exactly one " +
                    "candidate, show it to the user and get explicit confirmation before calling this — unless the user " +
                    "already unambiguously named that exact todo (e.g. gave the id or exact title) in their request."
    )
    public TaskActionResult deleteTodo(@McpToolParam(description = "The todo's id") Long todoId) {
        return coreService.deleteTodo(todoId)
                .map(todo -> TaskActionResult.success(todo, "Todo with title " + todo.getTitle() + " deleted"))
                .orElse(TaskActionResult.notFound(todoId));
    }

    @McpTool(
            name = "complete_todo",
            description = "Mark a todo as completed by its id. If you don't have a confident id for the todo the user " +
                    "described, call find_todos first. Even with a single confident match, confirm the exact todo with " +
                    "the user before calling this — unless the user already unambiguously named it (e.g. gave the id or " +
                    "exact title) in their request."
    )
    public TaskActionResult completeTodo(@McpToolParam(description = "The todo's id") Long todoId) {
        return coreService.completeTodo(todoId)
                .map(todo -> TaskActionResult.success(todo, "Todo with title " + todo.getTitle() + " marked completed."))
                .orElse(TaskActionResult.notFound(todoId));

    }

    @McpTool(
            name = "update_todo",
            description = "Update one or more fields of an existing todo by its id. Only the fields you supply are " +
                    "changed; omitted fields are left as-is. If you don't have a confident id for the todo the user " +
                    "described, call find_todos first. Even with a single confident match, confirm the exact todo and " +
                    "the intended changes with the user before calling this — unless the user already unambiguously " +
                    "named the todo and the changes in their request."
    )
    public TaskActionResult updateTodo(UpdateTodoRequest updateTodoRequest) {
        return coreService.updateTodo(updateTodoRequest)
                .map(todo -> TaskActionResult.success(todo, "Todo with id " + updateTodoRequest.id() + " updated"))
                .orElse(TaskActionResult.notFound(updateTodoRequest.id()));
    }

    @McpTool(
            name = "find_todos",
            description = "Search pending todos by text matched against the title (case-insensitive, partial match). " +
                    "Use this whenever the user refers to a todo by name or description rather than by id, before calling " +
                    "complete_todo, delete_todo, or update_todo. Returns candidates with their ids. Always show the " +
                    "result to the user and get explicit confirmation of which todo they mean before acting on it — this " +
                    "applies even when exactly one candidate is returned, since a single match is not the same as certainty."
    )
    public List<TodoResponse> findTodos(@McpToolParam(description = "Search text to match against todo titles") String query) {
        return coreService.findMatchingTodos(query);
    }
}
