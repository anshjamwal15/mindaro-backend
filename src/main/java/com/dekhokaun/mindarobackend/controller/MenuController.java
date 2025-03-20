package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.response.MenuResponse;
import com.dekhokaun.mindarobackend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu Controller", description = "APIs for managing menu items")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "Get all menu items", description = "Retrieves a list of all menu items")
    @GetMapping("/list")
    public ResponseEntity<List<MenuResponse>> getMenus() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @Operation(summary = "Delete a menu item", description = "Deletes a menu item by its text identifier")
    @DeleteMapping("/delete/{text}")
    public ResponseEntity<Void> deleteMenu(
            @Parameter(description = "Text identifier of the menu item to delete", required = true) @PathVariable String text) {
        menuService.deleteMenu(text);
        return ResponseEntity.noContent().build();
    }
}
