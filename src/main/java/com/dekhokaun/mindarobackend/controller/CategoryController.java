package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.CategoryDto;
import com.dekhokaun.mindarobackend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
