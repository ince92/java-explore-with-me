package ru.practicum.ewm_main.users.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;

    private Boolean subscriptionWithConfirm;
}