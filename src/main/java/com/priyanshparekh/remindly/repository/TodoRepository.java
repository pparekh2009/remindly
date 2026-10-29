package com.priyanshparekh.remindly.repository;

import com.priyanshparekh.remindly.dto.CategoryProjection;
import com.priyanshparekh.remindly.dto.PriorityProjection;
import com.priyanshparekh.remindly.entity.Priority;
import com.priyanshparekh.remindly.entity.Todo;
import lombok.NonNull;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<@NonNull Todo, @NonNull Long> {

    List<Todo> findAllByCompletedAtIsNull();

    List<Todo> findAllByCompletedAtIsNull(Limit limit);

    List<Todo> findByTitleContainingIgnoreCaseAndCompletedAtIsNull(String title);

    List<Todo> findAllByCompletedAtIsNullAndPriority(Priority priority);

    List<Todo> findAllByDueAtGreaterThanEqualAndDueAtLessThanAndCompletedAtIsNull(LocalDateTime start, LocalDateTime end);

    List<Todo> findAllByDueAtLessThanAndCompletedAtIsNull(LocalDateTime lessThan);

    List<Todo> findAllByCategoryIdAndCompletedAtIsNull(Long categoryId);

    long countByDueAtLessThanAndCompletedAtIsNull(LocalDateTime lessThan);

    long countByCompletedAtGreaterThanEqualAndCompletedAtLessThan(LocalDateTime start, LocalDateTime end);

    List<Todo> findAllByDueAtIsNullAndPriorityAndCompletedAtIsNull(Priority priority, Limit limit);

    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end);

    long countByDueAtGreaterThanEqualAndDueAtLessThanAndCompletedAtIsNull(LocalDateTime start, LocalDateTime end);

    long countByCompletedAtIsNull();

    long countByDueAtIsNullAndCompletedAtIsNull();

    @Query("SELECT count(t) AS count, t.priority AS priority FROM Todo t WHERE t.completedAt IS NULL GROUP BY t.priority")
    List<PriorityProjection> countByPriority();

//    @Query("SELECT count(t) AS count, c.category AS category FROM Todo t LEFT JOIN Category c ON t.category.id = c.id WHERE t.completedAt IS NULL GROUP BY c.category")
    @Query("SELECT count(t) AS count, c.category AS category FROM Todo t LEFT JOIN t.category c WHERE t.completedAt IS NULL GROUP BY c.category")
    List<CategoryProjection> countByCategory();

    List<Todo> findAllByDueAtLessThanAndCompletedAtIsNullOrderByPriorityDescDueAtAsc(LocalDateTime lessThan, Limit limit);
}
