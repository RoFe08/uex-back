package org.uex_back.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uex_back.dto.auth.AuthResponse;
import org.uex_back.dto.login.LoginRequest;
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

        boolean senhaOk = passwordEncoder.matches(request.password(), user.getPasswordHash());

        if (!senhaOk) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        String token = jwtService.generateToken(user); // retorna JWT

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public void deleteCurrentUser(String password) {
        User user = getCurrentUser();

        boolean matchPassword = passwordEncoder.matches(password, user.getPasswordHash());
        if (!matchPassword) {
            throw new BadCredentialsException("Senha inválida");
        }

        userRepository.delete(user);
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}

