package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.payload.request.CategoryRequest;
import com.dekhokaun.mindarobackend.payload.response.CategoryResponse;
import com.dekhokaun.mindarobackend.repository.CategoryRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = ObjectMapperUtils.map(request, Category.class);
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> ObjectMapperUtils.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public CategoryResponse updateCategory(String name, CategoryRequest request) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getDescription());
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public void deleteCategory(String name) {
        categoryRepository.deleteByName(name);
    }
}
