package ru.jabki.final_user.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabki.final_user.model.UserResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<UserResponse> {

    @Override
    public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserResponse.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .build();
    }
}
