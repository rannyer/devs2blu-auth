package com.project.auth_service.demo.repositories;

import com.project.auth_service.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    UserDetails findByLogin(String login);
}
