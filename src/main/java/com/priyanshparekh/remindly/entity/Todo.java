package com.priyanshparekh.remindly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Builder.Default
    private Priority priority = Priority.LOW;

    @PrePersist
    private void setCreatedDate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setModifiedDate() {
        modifiedAt = LocalDateTime.now();
    }
}
