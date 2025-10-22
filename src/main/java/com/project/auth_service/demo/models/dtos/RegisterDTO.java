package com.project.auth_service.demo.models.dtos;

import com.project.auth_service.demo.models.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
