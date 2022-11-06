package ru.practicum.ewm_main.categories.model.dto;

import ru.practicum.ewm_main.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
