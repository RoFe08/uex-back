package org.uex_back.dto.auth;

public record AuthResponse(
        String token,
        Long id,
        String name,
        String email
) {}