package org.uex_back.service.maps;

import org.uex_back.dto.maps.LatitudeLongitudeDTO;

public interface GeolocalizacaoStrategy {
    LatitudeLongitudeDTO geocode(String addressFormat);
}
