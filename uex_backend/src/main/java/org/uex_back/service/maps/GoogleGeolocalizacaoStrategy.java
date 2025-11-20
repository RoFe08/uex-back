package org.uex_back.service.maps;

import org.uex_back.dto.maps.GoogleGeocodingResponse;
import org.uex_back.dto.maps.LatitudeLongitudeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service("googleGeolocalizacao")
public class GoogleGeolocalizacaoStrategy implements GeolocalizacaoStrategy {

    private final RestTemplate restTemplate;

    @Value("${google.maps.geocoding.api-key}")
    private String apiKey;

    public GoogleGeolocalizacaoStrategy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public LatitudeLongitudeDTO geocode(String enderecoFormatado) {
        if (enderecoFormatado == null || enderecoFormatado.isBlank()) {
            return null;
        }

        try {
            String encodedAddress = URLEncoder.encode(enderecoFormatado, StandardCharsets.UTF_8);
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                    encodedAddress,
                    apiKey
            );

            GoogleGeocodingResponse response =
                    restTemplate.getForObject(url, GoogleGeocodingResponse.class);

            if (response == null ||
                    response.getResults() == null ||
                    response.getResults().isEmpty()) {
                return null;
            }

            var location = response.getResults().get(0)
                    .getGeometry()
                    .getLocation();

            if (location == null || location.getLat() == null || location.getLng() == null) {
                return null;
            }

            return new LatitudeLongitudeDTO(location.getLat(), location.getLng());

        } catch (Exception e) {
            // aqui em prod logaria bonitinho
            return null;
        }
    }
}