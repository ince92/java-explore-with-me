package ru.practicum.ewm_main.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.categories.model.dto.CategoryDto;
import ru.practicum.ewm_main.categories.model.dto.CategoryMapper;
import ru.practicum.ewm_main.categories.model.dto.NewCategoryDto;
import ru.practicum.ewm_main.categories.repository.CategoryRepository;
import ru.practicum.ewm_main.events.repository.EventRepository;
import ru.practicum.ewm_main.exception.NotFoundException;
import ru.practicum.ewm_main.exception.ValidationException;
import ru.practicum.ewm_main.categories.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).stream().map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категория с таким id не найдена!")));
    }

    public CategoryDto updateAdminCategory(CategoryDto category) {
        if (categoryRepository.getCategoryByName(category.getName()).isPresent()) {
            throw new ValidationException("Неуникальное имя категории!");
        }
        Category updatedCategory = categoryRepository.findById(category.getId()).orElseThrow(() ->
                new NotFoundException("Категория с таким id не найдена!"));
        updatedCategory.setName(category.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));

    }

    public CategoryDto createAdminCategory(NewCategoryDto category) {
        if (categoryRepository.getCategoryByName(category.getName()).isPresent()) {
            throw new ValidationException("Неуникальное имя категории!");
        }
        Category newCategory = new Category(0L, category.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(newCategory));

    }

    public void deleteAdminCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категория с таким id не найдена!"));
        if (!eventRepository.getEventsByCategoryId(id).isEmpty()) {
            throw new ValidationException("Есть события с такой категорией! Удаление не возможно!");
        }
        categoryRepository.delete(category);
    }
}
