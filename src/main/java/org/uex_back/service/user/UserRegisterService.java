package org.uex_back.service.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        var user = modelMapper.map(request, User.class);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

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
