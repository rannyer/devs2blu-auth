package com.project.auth_service.demo.services;

import com.project.auth_service.demo.models.User;
import com.project.auth_service.demo.models.UserRole;
import com.project.auth_service.demo.models.dtos.CreateUserDTO;
import com.project.auth_service.demo.repositories.UserRepository;
import com.project.auth_service.demo.shared.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private InMemoryUserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> "encoded:" + invocation.getArgument(0));
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void shouldFailWhenDtoIsNull() {
        Result<User> result = userService.create(null);

        assertThat(result.getErrors()).containsExactly("Request body is required");
        assertThat(userRepository.users).isEmpty();
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        Result<User> result = userService.create(new CreateUserDTO(" ", "secret", null));

        assertThat(result.getErrors()).containsExactly("Email is required");
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        Result<User> result = userService.create(new CreateUserDTO("not-an-email", "secret", null));

        assertThat(result.getErrors()).containsExactly("Email is invalid");
    }

    @Test
    void shouldFailWhenPasswordIsBlank() {
        Result<User> result = userService.create(new CreateUserDTO("john@mail.com", "", null));

        assertThat(result.getErrors()).containsExactly("Password is required");
    }

    @Test
    void shouldFailWhenEmailIsAlreadyInUse() {
        userRepository.save(new User("john@mail.com", "encoded:secret", UserRole.USER));

        Result<User> result = userService.create(new CreateUserDTO(" John@Mail.com ", "secret", null));

        assertThat(result.getErrors()).containsExactly("Email is already in use");
        assertThat(userRepository.users).hasSize(1);
    }

    @Test
    void shouldCreateUserWithNormalizedEmailEncodedPasswordAndDefaultRole() {
        Result<User> result = userService.create(new CreateUserDTO(" John@Mail.com ", "secret", null));

        assertThat(result.isSuccess()).isTrue();
        User user = result.getValue();
        assertThat(user.getLogin()).isEqualTo("john@mail.com");
        assertThat(user.getPassword()).isEqualTo("encoded:secret");
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
        assertThat(userRepository.users).containsExactly(user);
    }

    @Test
    void shouldKeepRoleWhenProvided() {
        Result<User> result = userService.create(new CreateUserDTO("vet@mail.com", "secret", UserRole.VET));

        assertThat(result.getValue().getRole()).isEqualTo(UserRole.VET);
    }

    private static class InMemoryUserRepository implements UserRepository {

        private final List<User> users = new ArrayList<>();

        @Override
        public Optional<User> findByLogin(String login) {
            return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
        }

        @Override
        public boolean existsByLogin(String login) {
            return findByLogin(login).isPresent();
        }

        @Override
        public User save(User user) {
            users.add(user);
            return user;
        }
    }
}
