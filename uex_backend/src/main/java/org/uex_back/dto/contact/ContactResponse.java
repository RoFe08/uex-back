package org.uex_back.dto.contact;

public record ContactResponse(
        Long id,
        String name,
        String cpf,
        String phone,
        String cep,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        Double latitude,
        Double longitude
) {}