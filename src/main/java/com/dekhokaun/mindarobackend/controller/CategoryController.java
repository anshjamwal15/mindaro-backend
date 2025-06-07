package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.response.CategoryResponse;
import com.dekhokaun.mindarobackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Controller", description = "APIs for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all available categories")
    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its name")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Name of the category to delete", required = true) @PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
