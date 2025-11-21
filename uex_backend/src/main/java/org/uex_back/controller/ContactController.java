package org.uex_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.uex_back.dto.contact.ContactPageResponse;
import org.uex_back.dto.contact.ContactRequest;
import org.uex_back.dto.contact.ContactResponse;
import org.uex_back.service.contact.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<ContactPageResponse> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String term) {
        ContactPageResponse result = contactService.list(term, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idContact}")
    public ResponseEntity<ContactResponse> getById(@PathVariable Long idContact) {
        ContactResponse response = contactService.getById(idContact);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest contactRequest) {
        ContactResponse response = contactService.create(contactRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idContact}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long idContact, @Valid @RequestBody ContactRequest contactRequest) {
        ContactResponse response = contactService.update(idContact, contactRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idContact}")
    public ResponseEntity<Void> delete(@PathVariable Long idContact) {
        contactService.delete(idContact);
        return ResponseEntity.noContent().build();
    }
}