package org.uex_back.dto.contact;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {

    @NotBlank @Size(max = 120)
    private String name;

    @NotBlank @Size(max = 14)
    private String cpf;

    @NotBlank @Size(max = 20)
    private String phone;

    @NotBlank @Size(max = 9)
    private String cep;

    @NotBlank @Size(max = 255)
    private String street;

    @NotBlank @Size(max = 20)
    private String number;

    @Size(max = 255)
    private String complement;

    @NotBlank @Size(max = 120)
    private String neighborhood;

    @NotBlank @Size(max = 120)
    private String city;

    @NotBlank @Size(max = 2)
    private String state;
}
