package com.project.auth_service.demo.services;

import com.project.auth_service.demo.models.User;
import com.project.auth_service.demo.models.UserRole;
import com.project.auth_service.demo.models.dtos.CreateUserDTO;
import com.project.auth_service.demo.repositories.UserRepository;
import com.project.auth_service.demo.shared.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Result<User> create(CreateUserDTO dto) {
        List<String> errors = validate(dto);
        if (!errors.isEmpty()) return Result.failure(errors);

        return Result.success(persist(dto));
    }

    private List<String> validate(CreateUserDTO dto) {
        List<String> errors = validateInput(dto);
        if (errors.isEmpty() && userRepository.existsByLogin(normalizeEmail(dto.email()))) {
            errors.add("Email is already in use");
        }
        return errors;
    }

    private User persist(CreateUserDTO dto) {
        UserRole role = dto.role() != null ? dto.role() : UserRole.USER;
        User user = new User(normalizeEmail(dto.email()), passwordEncoder.encode(dto.password()), role);
        return userRepository.save(user);
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private List<String> validateInput(CreateUserDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Request body is required");
            return errors;
        }
        if (dto.email() == null || dto.email().isBlank()) {
            errors.add("Email is required");
        } else if (!EMAIL_PATTERN.matcher(dto.email().trim()).matches()) {
            errors.add("Email is invalid");
        }
        if (dto.password() == null || dto.password().isBlank()) {
            errors.add("Password is required");
        }
        return errors;
    }
}
