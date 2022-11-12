package ru.practicum.ewm_main.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.users.model.dto.NewUserRequest;
import ru.practicum.ewm_main.users.model.dto.UserDto;
import ru.practicum.ewm_main.users.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserServiceImpl userService;

    @GetMapping()
    public List<UserDto> getUsers(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                  @RequestParam(name = "ids",required = false) List<Long> ids) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Получаем пользователей");
        return userService.getUsers(ids, pageRequest);

    }

    @PostMapping()
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUser) {
        log.info("Добавляем пользователя- {}", newUser.getName());
        return userService.createUser(newUser);

    }

    @DeleteMapping(value = "/{userid}")
    public void deleteUser(@PathVariable("userid") Long userid) {
        log.info("Удаляем пользователя- {}", userid);
        userService.deleteUser(userid);

    }
}
