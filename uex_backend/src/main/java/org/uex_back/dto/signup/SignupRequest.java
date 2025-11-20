package org.uex_back.dto.signup;

public record SignupRequest(
        String name,
        String email,
        String password
) {}

