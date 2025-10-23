package com.project.auth_service.demo.controllers;

import com.project.auth_service.demo.infra.security.TokenService;
import com.project.auth_service.demo.models.User;
import com.project.auth_service.demo.models.dtos.AuthenticationDTO;
import com.project.auth_service.demo.models.dtos.LoginResponseDTO;
import com.project.auth_service.demo.models.dtos.RegisterDTO;
import com.project.auth_service.demo.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO dto){
        var userPassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = authenticationManager.authenticate(userPassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());
        return  ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO dto){
        if(this.userRepository.findByLogin(dto.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user =  new User(dto.login(), encryptedPassword, dto.role());
        userRepository.save(user);
        return ResponseEntity.ok().build();

    }
}
