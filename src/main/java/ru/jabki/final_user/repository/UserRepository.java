package ru.jabki.final_user.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabki.final_user.exception.BadRequestException;
import ru.jabki.final_user.model.User;
import ru.jabki.final_user.model.UserResponse;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO final_user.user (username, password, created_at)
            VALUES (:username, :password, now())
            RETURNING *;
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM final_user.user
            WHERE id = :id
            AND deleted_at IS NULL
            """;

    private static final String LIST = """
            SELECT *
            FROM final_user.user
            WHERE deleted_at IS NULL
            """;

    private static final String DELETE = """
            UPDATE final_user.user
            SET deleted_at = now()
            WHERE id = :id
            AND deleted_at IS NULL
            """;

    private final UserMapper userMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserResponse insert(final User user) {
        try {
            return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
        } catch (DataAccessException e) {
            if (e.getMessage().contains("\"user_idx01\"")) {
                throw new BadRequestException(String.format("Пользователь '%s' уже существует", user.getUsername()));
            }

            throw e;
        }
    }

    public UserResponse getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (DataAccessException e) {
            throw new BadRequestException(String.format("Пользователь с id %s не найден", id));
        }
    }

    public List<UserResponse> list() {
        return jdbcTemplate.query(LIST, new MapSqlParameterSource(), userMapper);
    }

    public void delete(final Long id) {
        if (jdbcTemplate.update(DELETE, new MapSqlParameterSource("id", id)) == 0) {
            throw new BadRequestException(String.format("Пользователь с id %s не найден", id));
        }
    }

    public boolean existsById(final Long id) {
        try {
            jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public MapSqlParameterSource userToSql(final User user) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());

        return params;
    }
}
