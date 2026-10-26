package com.project.auth_service.demo.models.dtos;

import com.project.auth_service.demo.models.UserRole;

public record CreateUserDTO(String email, String password, UserRole role) {
}
