package org.uex_back.dto.login;

public record LoginRequest(
        String email,
        String password
) {}

