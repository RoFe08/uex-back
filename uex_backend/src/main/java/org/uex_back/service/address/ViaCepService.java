package org.uex_back.service.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uex_back.dto.address.AddressSuggestion;
import org.uex_back.dto.address.ViaCepResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViaCepService implements AddressLookupService {

    private static final String BASE_URL = "https://viacep.com.br/ws";

    private final RestTemplate restTemplate;

    @Override
    public List<AddressSuggestion> search(String uf, String city, String street) {
        if (isBlank(uf) || isBlank(city) || isBlank(street)) {
            return List.of();
        }

        String url = String.format(
                "%s/%s/%s/%s/json/",
                BASE_URL,
                uf.trim(),
                encode(city),
                encode(street)
        );

        ViaCepResponse[] response = restTemplate.getForObject(url, ViaCepResponse[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.stream(response)
                .filter(this::isValid)
                .map(this::toSuggestion)
                .toList();
    }

    @Override
    public Optional<AddressSuggestion> findByCep(String rawCep) {
        String cep = normalizeCep(rawCep);
        if (cep == null) {
            return Optional.empty();
        }

        String url = String.format("%s/%s/json/", BASE_URL, cep);
        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (!isValid(response)) {
            return Optional.empty();
        }

        return Optional.of(toSuggestion(response));
    }

    private boolean isValid(ViaCepResponse response) {
        return response != null && !Boolean.TRUE.equals(response.getErro());
    }

    private AddressSuggestion toSuggestion(ViaCepResponse r) {
        return new AddressSuggestion(
                r.getCep(),
                r.getLogradouro(),
                r.getBairro(),
                r.getLocalidade(),
                r.getUf()
        );
    }

    private String normalizeCep(String rawCep) {
        if (rawCep == null) return null;
        String digits = rawCep.replaceAll("\\D", "");
        return digits.length() == 8 ? digits : null;
    }

    private String encode(String value) {
        return URLEncoder.encode(value.trim(), StandardCharsets.UTF_8);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

