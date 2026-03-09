package ru.jabki.final_user.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Role {
    USER(1),
    MANAGER(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    // https://stackoverflow.com/questions/27484353/gettin-enum-types-may-not-be-instantiated-exception
    final static Map<Integer, Role> map = new HashMap<>();

    static {
        for (Role role : Role.values()) {
            map.put(role.id, role);
        }
    }

    public static Role getById(int id) {
        return map.get(id);
    }
}
