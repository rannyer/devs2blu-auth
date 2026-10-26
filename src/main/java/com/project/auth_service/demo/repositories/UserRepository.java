package com.project.auth_service.demo.repositories;

import com.project.auth_service.demo.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    User save(User user);
}
