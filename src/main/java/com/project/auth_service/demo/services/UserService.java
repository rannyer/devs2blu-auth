package com.project.auth_service.demo.services;

import com.project.auth_service.demo.models.User;
import com.project.auth_service.demo.models.dtos.CreateUserDTO;
import com.project.auth_service.demo.shared.Result;

public interface UserService {
    Result<User> create(CreateUserDTO dto);
}
