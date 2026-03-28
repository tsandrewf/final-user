package ru.jabki.final_user.exception;

public class UserByNameNotFoundException extends RuntimeException {

    public UserByNameNotFoundException(final String name) {
        super(String.format("Пользователь '%s' не найден", name));
    }
}
