package ru.jabki.final_user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserCredentials {

    private String username;
    private Role role;
}
