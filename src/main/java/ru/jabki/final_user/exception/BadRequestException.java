package ru.jabki.final_user.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }
}
