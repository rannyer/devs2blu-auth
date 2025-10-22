package com.project.auth_service.demo.repositories;

import com.project.auth_service.demo.models.Animal;
import com.project.auth_service.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepositorie extends JpaRepository<Animal, Long>{
}

