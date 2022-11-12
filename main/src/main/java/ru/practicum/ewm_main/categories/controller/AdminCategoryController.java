package ru.practicum.ewm_main.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.categories.model.dto.CategoryDto;
import ru.practicum.ewm_main.categories.model.dto.NewCategoryDto;
import ru.practicum.ewm_main.categories.service.CategoryServiceImpl;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryServiceImpl categoryService;

    @PatchMapping()
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto category) {
        log.info("Обновление категории - {}", category.getId());
        return categoryService.updateAdminCategory(category);
    }

    @PostMapping()
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto category) {
        log.info("Создание категории - {}", category.getName());
        return categoryService.createAdminCategory(category);
    }

    @DeleteMapping(value = "/{catId}")
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Удаление категории - {}", catId);
        categoryService.deleteAdminCategoryById(catId);
    }
}
