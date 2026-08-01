package com.priyanshparekh.remindly.repository;

import com.priyanshparekh.remindly.entity.Todo;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<@NonNull Todo, @NonNull Long> {
}
