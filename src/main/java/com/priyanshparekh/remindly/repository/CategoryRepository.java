package com.priyanshparekh.remindly.repository;

import com.priyanshparekh.remindly.dto.CategoriesProjection;
import com.priyanshparekh.remindly.entity.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<@NonNull Category, @NonNull Long> {

    Optional<Category> findByCategoryIgnoreCase(String category);

    @Query(value = """
                    SELECT c.category as category, COUNT(t.id) as pendingTasks FROM categories c LEFT JOIN todos t ON t.category_id = c.id AND t.completed_at IS NULL GROUP BY c.category ORDER BY c.category
                    """,
    nativeQuery = true)
    List<CategoriesProjection> getAllCategoriesWithPendingTasks();
}
