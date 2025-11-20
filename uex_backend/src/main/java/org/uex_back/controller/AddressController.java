package org.uex_back.controller;

import lombok.RequiredArgsConstructor;
import org.uex_back.dto.address.AddressSuggestion;
import org.uex_back.service.address.ViaCepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final ViaCepService viaCepService;

    // Ex: GET /api/address/search?uf=PR&city=Curitiba&street=Rua%20XV
    @GetMapping("/search")
    public ResponseEntity<List<AddressSuggestion>> search(
            @RequestParam String uf,
            @RequestParam String city,
            @RequestParam String street
    ) {
        List<AddressSuggestion> result = viaCepService.search(uf, city, street);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-cep")
    public ResponseEntity<AddressSuggestion> findByCep(@RequestParam String cep) {
        var suggestion = viaCepService.findByCep(cep);
        if (suggestion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(suggestion);
    }
}