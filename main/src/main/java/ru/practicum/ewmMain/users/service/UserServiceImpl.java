package ru.practicum.ewmMain.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmMain.exception.NotFoundException;
import ru.practicum.ewmMain.users.model.dto.NewUserRequest;
import ru.practicum.ewmMain.users.model.dto.UserDto;
import ru.practicum.ewmMain.users.model.dto.UserMapper;
import ru.practicum.ewmMain.users.model.User;
import ru.practicum.ewmMain.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Pageable pageRequest) {
        return userRepository.getUsers(ids, pageRequest).stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(NewUserRequest newUser) {
        return UserMapper.toUserDto(userRepository.save(new User(0L, newUser.getName(), newUser.getEmail())));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        userRepository.delete(user);
    }
}
