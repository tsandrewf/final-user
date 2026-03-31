package ru.jabki.final_user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.jabki.final_user.model.User;
import ru.jabki.final_user.model.UserCredentials;
import ru.jabki.final_user.model.UserResponse;
import ru.jabki.final_user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public UserResponse create(@RequestBody final User user) {
        return userService.create(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserResponse getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "Получение списка пользователей")
    public List<UserResponse> list() {
        return userService.list();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    @ResponseBody
    public Map<String, Boolean> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return Collections.singletonMap("success", true);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Проверка, существует ли пользователь с id")
    public boolean existsById(@PathVariable("id") Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/credentials/{username}")
    @Operation(summary = "Получить реквизиты пользователя для входа")
    public UserCredentials getCredentials(@PathVariable("username") String username) {
        return userService.getCredentials(username);
    }

    @GetMapping("/role/{id}")
    @Operation(summary = "Получить роль пользователя")
    public String getRoleById(@PathVariable("id") Long id) {
        return userService.getRoleById(id);
    }
}
