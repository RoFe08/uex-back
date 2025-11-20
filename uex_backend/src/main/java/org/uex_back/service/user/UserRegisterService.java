package org.uex_back.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uex_back.dto.auth.AuthResponse;
import org.uex_back.dto.signup.SignupRequest;
import org.uex_back.exceptionhandler.EmailAlreadyInUseException;
import org.uex_back.model.User;
import org.uex_back.repository.UserRepository;
import org.uex_back.security.JwtService;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyInUseException(request.email());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved);

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }
}
