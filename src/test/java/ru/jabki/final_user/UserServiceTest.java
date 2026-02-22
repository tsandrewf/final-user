package ru.jabki.final_user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.jabki.final_user.exception.UserException;
import ru.jabki.final_user.model.User;
import ru.jabki.final_user.model.UserResponse;
import ru.jabki.final_user.repository.UserRepository;
import ru.jabki.final_user.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_valid() {
        final User user = getUser();
        final UserResponse userResponse = getUserResponse();

        Mockito.when(userRepository.insert(user)).thenReturn(userResponse);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("EncodedPassword");

        UserResponse result = userService.create(user);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).insert(user);
    }

    @Test
    void createUser_nullName_throwsUserException() {
        final User user = getUser();
        user.setUsername(null);

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Имя пользователя не задано");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_nullPassword_throwsUserException() {
        final User user = getUser();
        user.setPassword(null);

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не задан");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordNoUpperCase_throwsUserException() {
        final User user = getUser();
        user.setPassword("password2$");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordNoLowerCase_throwsUserException() {
        final User user = getUser();
        user.setPassword("PASSWORD2$");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordNoDigit_throwsUserException() {
        final User user = getUser();
        user.setPassword("Passwordd$");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordNoSpecialChar_throwsUserException() {
        final User user = getUser();
        user.setPassword("Password22");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordNoSpace_throwsUserException() {
        final User user = getUser();
        user.setPassword("Passwor 2$");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordLess8_throwsUserException() {
        final User user = getUser();
        user.setPassword("Passw2$");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    @Test
    void createUser_badPasswordMore20_throwsUserException() {
        final User user = getUser();
        user.setPassword("Passw2$89012345678901");

        final UserException exception = assertThrows(
                UserException.class,
                () -> userService.create(user)
        );

        assertEquals(exception.getMessage(), "Пароль не удовлетворяет требованиям сложности: 1) длина не меньше 8 и не больше 20; 2) должен содержать цифры, латинские буквы в нижнем и верхнем регистрах, специальные символы; 3) не должен содержать пробелы");

        verify(userRepository, never()).insert(any());
    }

    private User getUser() {
        return User.builder()
                .username("Test Name")
                .password("Password2$")
                .build();
    }

    private UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(1L)
                .username("Test Name")
                .build();
    }
}
