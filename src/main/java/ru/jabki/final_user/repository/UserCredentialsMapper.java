package ru.jabki.final_user.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabki.final_user.model.Role;
import ru.jabki.final_user.model.UserCredentials;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserCredentialsMapper implements RowMapper<UserCredentials> {

    @Override
    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserCredentials.builder()
                .username(rs.getString("username"))
                .role(Role.getById(rs.getInt("role")))
                .build();
    }
}
