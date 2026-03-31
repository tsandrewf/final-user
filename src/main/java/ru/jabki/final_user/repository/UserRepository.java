package ru.jabki.final_user.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabki.final_user.exception.BadRequestException;
import ru.jabki.final_user.exception.UserByIdNotFoundException;
import ru.jabki.final_user.exception.UserByNameNotFoundException;
import ru.jabki.final_user.model.Role;
import ru.jabki.final_user.model.User;
import ru.jabki.final_user.model.UserCredentials;
import ru.jabki.final_user.model.UserResponse;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO final_user.user (username, password, role, created_at)
            VALUES (:username, :password, :role, now())
            RETURNING *;
            """;

    private static final String GET_BY_ID = """
            SELECT id, username
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

    private static final String EXISTS_BY_ID = """
            SELECT EXISTS (
                SELECT 1
                FROM final_user.user
                WHERE id = :id
                AND deleted_at is null
            )
            """;

    private static final String GET_CREDENTIALS = """
            SELECT username, role
            FROM final_user.user
            WHERE username = :username
            AND deleted_at IS NULL
            """;

    private static final String GET_ROLE_BY_ID = """
            SELECT role
            FROM final_user.user
            WHERE id = :id
            AND deleted_at IS NULL
            """;

    private final UserMapper userMapper;
    private final UserCredentialsMapper userCredentialsMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserResponse insert(final User user) {
        try {
            return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
        } catch (DuplicateKeyException e) {
                throw new BadRequestException(String.format("Пользователь '%s' уже существует", user.getUsername()));
        }
    }

    public UserResponse getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (DataAccessException e) {
            throw new UserByIdNotFoundException(id);
        }
    }

    public List<UserResponse> list() {
        return jdbcTemplate.query(LIST, new MapSqlParameterSource(), userMapper);
    }

    public void delete(final Long id) {
        if (jdbcTemplate.update(DELETE, new MapSqlParameterSource("id", id)) == 0) {
            throw new UserByIdNotFoundException(id);
        }
    }

    public boolean existsById(final Long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_ID, new MapSqlParameterSource("id", id), Boolean.class));
    }

    public UserCredentials getCredentials(final String username) {
        try {
            return jdbcTemplate.queryForObject(GET_CREDENTIALS, new MapSqlParameterSource("username", username), userCredentialsMapper);
        } catch (DataAccessException e) {
            throw new UserByNameNotFoundException(username);
        }
    }

    public String getRoleById(final Long id) {
        try {
            return Role.getById(Objects.requireNonNull(jdbcTemplate.queryForObject(GET_ROLE_BY_ID, new MapSqlParameterSource("id", id), Long.class)).intValue()).toString();
        } catch (DataAccessException e) {
            throw new UserByIdNotFoundException(id);
        }
    }

    public MapSqlParameterSource userToSql(final User user) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("role", user.getRole().getId());

        return params;
    }
}
