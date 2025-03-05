package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.dto.MenuDto;
import com.dekhokaun.mindarobackend.model.Menu;
import com.dekhokaun.mindarobackend.payload.request.MenuRequest;
import com.dekhokaun.mindarobackend.repository.MenuRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuDto addMenuItem(MenuRequest request) {
        Menu menu = ObjectMapperUtils.map(request, Menu.class);
        menuRepository.save(menu);
        return ObjectMapperUtils.map(menu, MenuDto.class);
    }

    public List<MenuDto> getAllMenuItems() {
        return menuRepository.findAll().stream()
                .map(menu -> ObjectMapperUtils.map(menu, MenuDto.class))
                .collect(Collectors.toList());
    }

    public MenuDto getMenuItem(String text) {
        Menu menu = menuRepository.findByText(text)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        return ObjectMapperUtils.map(menu, MenuDto.class);
    }

    public void updateMenu(String text, MenuRequest request) {
        Menu menu = menuRepository.findByText(text)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        menu.setIcon(request.getIcon());
        menu.setAction(request.getAction());
        menu.setLink(request.getLink());
        menuRepository.save(menu);
    }

    public void deleteMenu(String text) {
        menuRepository.deleteByText(text);
    }
}
