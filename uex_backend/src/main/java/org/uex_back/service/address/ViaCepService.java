package org.uex_back.service.address;

import org.uex_back.dto.address.AddressSuggestion;
import org.uex_back.dto.address.ViaCepResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ViaCepService {

    private static final String VIACEP_URL_TEMPLATE =
            "https://viacep.com.br/ws/%s/%s/%s/json/";

    private static final String VIACEP_CEP_URL_TEMPLATE =
            "https://viacep.com.br/ws/%s/json/";


    public List<AddressSuggestion> search(String uf, String city, String street) {
        if (uf == null || city == null || street == null) {
            return Collections.emptyList();
        }

        try {
            String encodedCity = URLEncoder.encode(city.trim(), StandardCharsets.UTF_8.toString());
            String encodedStreet = URLEncoder.encode(street.trim(), StandardCharsets.UTF_8.toString());

            String url = String.format(VIACEP_URL_TEMPLATE, uf.trim(), encodedCity, encodedStreet);

            RestTemplate restTemplate = new RestTemplate();
            ViaCepResponse[] response = restTemplate.getForObject(url, ViaCepResponse[].class);

            if (response == null || response.length == 0) {
                return Collections.emptyList();
            }

            return Arrays.stream(response)
                    .filter(r -> r.getErro() == null || !r.getErro())
                    .map(r -> new AddressSuggestion(
                            r.getCep(),
                            r.getLogradouro(),
                            r.getBairro(),
                            r.getLocalidade(),
                            r.getUf()
                    ))
                    .toList();

        } catch (UnsupportedEncodingException e) {
            return Collections.emptyList();
        }
    }

    public AddressSuggestion findByCep(String rawCep) {
        if (rawCep == null) {
            return null;
        }

        // Mantém só dígitos
        String cep = rawCep.replaceAll("\\D", "");

        if (cep.length() != 8) {
            return null;
        }

        String url = String.format(VIACEP_CEP_URL_TEMPLATE, cep);

        RestTemplate restTemplate = new RestTemplate();
        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (response == null || Boolean.TRUE.equals(response.getErro())) {
            return null;
        }

        return new AddressSuggestion(
                response.getCep(),
                response.getLogradouro(),
                response.getBairro(),
                response.getLocalidade(),
                response.getUf()
        );
    }
}