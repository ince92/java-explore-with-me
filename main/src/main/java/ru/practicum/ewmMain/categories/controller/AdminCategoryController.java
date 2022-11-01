package ru.practicum.ewmMain.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmMain.categories.model.dto.CategoryDto;
import ru.practicum.ewmMain.categories.model.dto.NewCategoryDto;
import ru.practicum.ewmMain.categories.service.CategoryServiceImpl;

@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryServiceImpl categoryService;

    @PatchMapping()
    public CategoryDto updateCategory(@RequestBody CategoryDto category) {
        log.info("Обновление категории - {}", category.getId());
        return categoryService.updateAdminCategory(category);
    }

    @PostMapping()
    public CategoryDto createCategory(@RequestBody NewCategoryDto category) {
        log.info("Создание категории - {}", category.getName());
        return categoryService.createAdminCategory(category);
    }

    @DeleteMapping(value = "/{catId}")
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Удаление категории - {}", catId);
        categoryService.deleteAdminCategoryById(catId);
    }
}
