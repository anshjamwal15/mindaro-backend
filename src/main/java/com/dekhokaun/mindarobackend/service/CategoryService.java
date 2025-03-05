package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.CategoryDto;
import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.payload.request.CategoryRequest;
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

    public CategoryDto createCategory(CategoryRequest request) {
        Category category = ObjectMapperUtils.map(request, Category.class);
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryDto.class);
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> ObjectMapperUtils.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return ObjectMapperUtils.map(category, CategoryDto.class);
    }

    public CategoryDto updateCategory(String name, CategoryRequest request) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getDescription());
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryDto.class);
    }

    public void deleteCategory(String name) {
        categoryRepository.deleteByName(name);
    }
}
