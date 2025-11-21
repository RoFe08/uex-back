package org.uex_back.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uex_back.dto.auth.AuthResponse;
import org.uex_back.dto.login.LoginRequest;
import org.uex_back.exceptionhandler.InvalidPasswordException;
import org.uex_back.model.User;
import org.uex_back.repository.UserRepository;
import org.uex_back.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // serviço que gera/valida JWT

    public AuthResponse login(LoginRequest request) {
        var user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        var matchPassword = passwordEncoder.matches(request.password(), user.getPasswordHash());

        if (!matchPassword) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        var token = jwtService.generateToken(user); // retorna JWT

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public void deleteCurrentUser(String password) {
        var user = getCurrentUser();

        var matchPassword = passwordEncoder.matches(password, user.getPasswordHash());
        if (!matchPassword) {
            throw new InvalidPasswordException();
        }

        userRepository.delete(user);
    }

    private User getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}

