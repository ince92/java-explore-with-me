package ru.practicum.ewmMain.categories.model.dto;

import ru.practicum.ewmMain.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
