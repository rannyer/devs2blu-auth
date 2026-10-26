package com.project.auth_service.demo.repositories.jpa;

import com.project.auth_service.demo.models.User;
import com.project.auth_service.demo.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public JpaUserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userJpaRepository.findByLogin(login);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userJpaRepository.existsByLogin(login);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
