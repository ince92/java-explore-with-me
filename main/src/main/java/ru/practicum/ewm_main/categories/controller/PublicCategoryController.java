package ru.practicum.ewm_main.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.categories.model.dto.CategoryDto;
import ru.practicum.ewm_main.categories.service.CategoryServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Получение категорий");
        return categoryService.getCategories(pageRequest);
    }

    @GetMapping(value = "/{id}")
    public CategoryDto getCategory(@PathVariable("id") Long id) {
        log.info("Получение категории по идентификатору - {}",id);
        return categoryService.getCategoryById(id);
    }
}
