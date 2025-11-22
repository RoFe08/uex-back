package org.uex_back.dto.address;

public record AddressSuggestion(
        String cep,
        String street,
        String neighborhood,
        String city,
        String state
) {}