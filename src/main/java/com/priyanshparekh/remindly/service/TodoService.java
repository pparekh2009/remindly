package com.priyanshparekh.remindly.service;

import com.priyanshparekh.remindly.dto.AddTodoRequest;
import com.priyanshparekh.remindly.dto.TodoListResult;
import com.priyanshparekh.remindly.dto.TodoResult;
import com.priyanshparekh.remindly.entity.Priority;
import com.priyanshparekh.remindly.entity.Todo;
import com.priyanshparekh.remindly.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void addTodo(AddTodoRequest addTodoRequest) {
        Todo todo = Todo.builder()
                .todo(addTodoRequest.todo())
                .priority(Priority.valueOf(addTodoRequest.priority()))
                .category(addTodoRequest.category())
                .dueAt(addTodoRequest.dueAt())
                .build();
        todoRepository.save(todo);
    }

    public TodoListResult getTodos() {
        List<TodoResult> todoResults = todoRepository.findAll().stream().map(todo -> {
            return TodoResult.builder()
                    .title(todo.getTodo())
                    .dueAt((todo.getDueAt() != null) ? todo.getDueAt().toString() : "")
                    .category(todo.getCategory())
                    .priority(todo.getPriority().name())
                    .build();
        }).toList();

        return new TodoListResult(todoResults);
    }

}
