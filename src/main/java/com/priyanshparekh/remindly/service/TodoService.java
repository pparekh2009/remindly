package com.priyanshparekh.remindly.service;

import com.priyanshparekh.remindly.dto.AddTodoRequest;
import com.priyanshparekh.remindly.dto.TodoResponse;
import com.priyanshparekh.remindly.dto.UpdateTodoRequest;
import com.priyanshparekh.remindly.entity.Priority;
import com.priyanshparekh.remindly.entity.Todo;
import com.priyanshparekh.remindly.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void addTodo(AddTodoRequest addTodoRequest) {
        Todo todo = Todo.builder()
                .title(addTodoRequest.title())
                .priority(Priority.valueOf(addTodoRequest.priority() != null ? addTodoRequest.priority() : "LOW"))
                .category(addTodoRequest.category())
                .dueAt(addTodoRequest.dueAt())
                .build();
        todoRepository.save(todo);
    }

    public List<TodoResponse> getAllPendingTodos() {
        return todoRepository.findAllByCompletedAtIsNull().stream().map(TodoResponse::from).toList();
    }

    @Transactional
    public Optional<Todo> completeTodo(long taskId) {
        return todoRepository.findById(taskId).map(todo -> {
            todo.setCompletedAt(LocalDateTime.now());
            return todoRepository.save(todo);
        });
    }

    public List<TodoResponse> findMatchingTodos(String query) {
        return todoRepository.findByTitleContainingIgnoreCaseAndCompletedAtIsNull(query).stream().map(TodoResponse::from).toList();
    }

    @Transactional
    public Optional<@NonNull Todo> updateTodo(UpdateTodoRequest updateTodoRequest) {
        return todoRepository.findById(updateTodoRequest.id()).map(todo -> {
            if (updateTodoRequest.title() != null) todo.setTitle(updateTodoRequest.title());
            if (updateTodoRequest.category() != null) todo.setCategory(updateTodoRequest.category());
            if (updateTodoRequest.priority() != null) todo.setPriority(Priority.valueOf(updateTodoRequest.priority().toUpperCase()));
            if (updateTodoRequest.dueAt() != null) todo.setDueAt(updateTodoRequest.dueAt());
            return todoRepository.save(todo);
        });
    }

    public Optional<@NonNull Todo> deleteTodo(Long todoId) {
        return todoRepository.findById(todoId).map(todo -> {
           todoRepository.delete(todo);
           return todo;
        });
    }
}
