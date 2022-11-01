package ru.practicum.ewmMain.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmMain.categories.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryByName(String name);

}
