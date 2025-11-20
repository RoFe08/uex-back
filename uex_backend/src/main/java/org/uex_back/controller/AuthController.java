package org.uex_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uex_back.dto.account.DeleteAccountRequest;
import org.uex_back.dto.auth.AuthResponse;
import org.uex_back.dto.login.LoginRequest;
import org.uex_back.dto.signup.SignupRequest;
import org.uex_back.service.auth.AuthService;
import org.uex_back.service.user.UserRegisterService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRegisterService userRegisterService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        AuthResponse response = userRegisterService.signup(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@RequestBody DeleteAccountRequest request) {
        authService.deleteCurrentUser(request.password());
        return ResponseEntity.noContent().build();
    }

}
