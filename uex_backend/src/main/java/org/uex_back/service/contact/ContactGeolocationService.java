package org.uex_back.service.contact;

import org.uex_back.model.Contact;
import org.uex_back.service.maps.GeolocalizacaoStrategy;
import org.uex_back.dto.maps.LatitudeLongitudeDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ContactGeolocationService {

    private final GeolocalizacaoStrategy geolocalizacaoStrategy;

    public ContactGeolocationService(
            @Qualifier("googleGeolocalizacao") GeolocalizacaoStrategy strategyGoogle
    ) {
        this.geolocalizacaoStrategy = strategyGoogle;
    }

    public void saveLatitudeLongitude(Contact contact) {
        contact.setLatitude(null);
        contact.setLongitude(null);

        atribuirLatitudeLongitude(contact)
                .ifPresent(latLng -> {
                    contact.setLatitude(latLng.getLatitude());
                    contact.setLongitude(latLng.getLongitude());
                });
    }

    public boolean invalidAddress(Contact contact) {
        return Stream.of(
                contact.getStreet(),
                contact.getNumber(),
                contact.getNeighborhood(),
                contact.getCity(),
                contact.getState()
        ).anyMatch(campo -> campo == null || campo.trim().isEmpty());
    }

    private String formatAddress(Contact contact) {
        return String.format(
                "%s, %s, %s, %s, %s",
                contact.getStreet(),
                contact.getNumber(),
                contact.getNeighborhood(),
                contact.getCity(),
                contact.getState()
        );
    }

    public Optional<LatitudeLongitudeDTO> atribuirLatitudeLongitude(Contact contact) {
        if (invalidAddress(contact)) {
            return Optional.empty();
        }

        String endereco = formatAddress(contact);
        return Optional.ofNullable(geolocalizacaoStrategy.geocode(endereco));
    }
}
