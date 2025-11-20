package org.uex_back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "contacts",
        uniqueConstraints = {
                // CPF não pode se repetir para o MESMO usuário
                @UniqueConstraint(
                        name = "uk_contact_user_cpf",
                        columnNames = {"user_id", "cpf"}
                )
        }
)
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 14) // 000.000.000-00
    @Column(nullable = false)
    private String cpf;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 9)
    private String cep;

    @NotBlank
    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 20)
    private String number;

    @Size(max = 255)
    private String complement; // único campo não obrigatório

    @NotBlank
    @Size(max = 120)
    private String neighborhood;

    @NotBlank
    @Size(max = 120)
    private String city;

    @NotBlank
    @Size(max = 2)
    private String state;

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
