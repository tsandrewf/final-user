package ru.jabki.final_user.model;

import lombok.Data;

@Data
public class ApiError {

    final boolean success;
    final String message;
}
