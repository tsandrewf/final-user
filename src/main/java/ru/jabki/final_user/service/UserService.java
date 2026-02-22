package ru.jabki.final_user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabki.final_user.exception.UserException;
import ru.jabki.final_user.model.User;
import ru.jabki.final_user.model.UserResponse;
import ru.jabki.final_user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse create(final User user) {
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.insert(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(final long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.list();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(final long id) {
        return userRepository.existsById(id);
    }

    private void validate(final User user) {
        if (user == null) {
            throw new UserException("Пользователь не задан");
        }
        if (!StringUtils.hasText(user.getUsername())) {
            throw new UserException("Имя пользователя не задано");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new UserException("Пароль не задан");
        }
        passwordValidate(user.getPassword());
    }

    private void passwordValidate(final String password) {
        if (!password.matches(getPattern())) {
            throw new UserException(
                    "Пароль не удовлетворяет требованиям сложности: " +
                    "1) длина не меньше 8 и не больше 20;" +
                    " 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы;" +
                    " 3) не должен содержать пробелы"
            );
        }
    }

    private static String getPattern() {
        final String MIN_LENGTH = "8";
        final String MAX_LENGTH = "20";
        final boolean SPECIAL_CHAR_NEEDED = true;

        final String ONE_DIGIT = "(?=.*[0-9])";
        final String LOWER_CASE = "(?=.*[a-z])";
        final String UPPER_CASE = "(?=.*[A-Z])";
        final String SPECIAL_CHAR = SPECIAL_CHAR_NEEDED ? "(?=.*[@#$%^&+=])" : "";
        final String NO_SPACE = "(?=\\S+$)";

        final String MIN_MAX_CHAR = ".{" + MIN_LENGTH + "," + MAX_LENGTH + "}";

        return ONE_DIGIT + LOWER_CASE + UPPER_CASE + SPECIAL_CHAR + NO_SPACE + MIN_MAX_CHAR;
    }
}
