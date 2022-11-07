package ru.practicum.ewm_main.users.model.dto;

import ru.practicum.ewm_main.users.model.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
