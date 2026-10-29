package com.priyanshparekh.remindly.discover;

import com.priyanshparekh.remindly.dto.CategoriesProjection;
import com.priyanshparekh.remindly.dto.CategoriesResponse;
import com.priyanshparekh.remindly.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoverService {

    private final CategoryRepository categoryRepository;

    public List<CategoriesResponse> getCategories() {
        return categoryRepository.getAllCategoriesWithPendingTasks()
                .stream().map(categoriesProjection ->
                        new CategoriesResponse(categoriesProjection.getCategory(), categoriesProjection.getPendingTasks())).toList();
    }

}
