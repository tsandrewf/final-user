package ru.jabki.final_user.exception;

public class UserByIdNotFoundException extends RuntimeException {

    public UserByIdNotFoundException(final long id) {
        super(String.format("Пользователь с id %s не найден", id));
    }
}
