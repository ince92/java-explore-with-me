package ru.practicum.ewm_main.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.exception.NotFoundException;
import ru.practicum.ewm_main.users.model.dto.NewUserRequest;
import ru.practicum.ewm_main.users.model.dto.UserDto;
import ru.practicum.ewm_main.users.model.dto.UserMapper;
import ru.practicum.ewm_main.users.model.User;
import ru.practicum.ewm_main.users.repository.UserRepository;

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
        Boolean flag = newUser.getSubscriptionWithConfirm() == null ? false : newUser.getSubscriptionWithConfirm();
        return UserMapper.toUserDto(userRepository.save(new User(0L, newUser.getName(), newUser.getEmail(), flag)));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        userRepository.delete(user);
    }
}
