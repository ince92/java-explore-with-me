package ru.practicum.ewm_main.categories.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
