package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.dto.MenuDto;
import com.dekhokaun.mindarobackend.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<MenuDto>> getMenus() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @DeleteMapping("/delete/{text}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String text) {
        menuService.deleteMenu(text);
        return ResponseEntity.noContent().build();
    }
}
