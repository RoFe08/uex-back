package org.uex_back.controller;

import lombok.RequiredArgsConstructor;
import org.uex_back.dto.address.AddressSuggestion;
import org.uex_back.service.address.AddressLookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressLookupService addressLookupService;

    @GetMapping("/search")
    public ResponseEntity<List<AddressSuggestion>> search(@RequestParam String uf, @RequestParam String city, @RequestParam String street) {
        List<AddressSuggestion> suggestions = addressLookupService.search(uf, city, street);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/by-cep")
    public ResponseEntity<AddressSuggestion> findByCep(@RequestParam String cep) {
        return addressLookupService.findByCep(cep)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}