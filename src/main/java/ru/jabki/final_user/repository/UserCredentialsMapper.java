package ru.jabki.final_user.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabki.final_user.model.Role;
import ru.jabki.final_user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserCredentialsMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .role(Role.getById(rs.getInt("role")))
                .build();
    }
}
