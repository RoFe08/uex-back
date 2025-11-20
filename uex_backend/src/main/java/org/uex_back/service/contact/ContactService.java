package org.uex_back.service.contact;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.uex_back.dto.contact.ContactPageResponse;
import org.uex_back.dto.contact.ContactRequest;
import org.uex_back.dto.contact.ContactResponse;
import org.uex_back.model.Contact;
import org.uex_back.model.User;
import org.uex_back.repository.ContactRepository;
import org.uex_back.repository.UserRepository;
import org.uex_back.validation.CpfValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactGeolocationService contactGeolocationService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public ContactPageResponse list(String term, int page, int size) {
        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        var pageResult = contactRepository.searchByUserAndTerm(user, term, pageable);

        var content = pageResult.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ContactPageResponse(
                content,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }

    public ContactResponse getById(Long id) {
        User user = getCurrentUser();

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        return toResponse(contact);
    }

    public ContactResponse create(ContactRequest request) {
        User user = getCurrentUser();

        validateCpf(request.getCpf());
        validateDuplicateCpf(user, request.getCpf(), null);

        Contact contact = modelMapper.map(request, Contact.class);
        contact.setUser(user);

        contactGeolocationService.saveLatitudeLongitude(contact);

        Contact saved = contactRepository.save(contact);
        return toResponse(saved);
    }

    public ContactResponse update(Long id, ContactRequest request) {
        User user = getCurrentUser();

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        validateCpf(request.getCpf());
        validateDuplicateCpf(user, request.getCpf(), id);

        modelMapper.map(request, contact);

        contactGeolocationService.saveLatitudeLongitude(contact);

        Contact updated = contactRepository.save(contact);
        return toResponse(updated);
    }

    public void delete(Long id) {
        User user = getCurrentUser();

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        contactRepository.delete(contact);
    }

    private void validateCpf(String cpf) {
        if (!CpfValidator.isValid(cpf)) {
            throw new RuntimeException("CPF inválido");
        }
    }

    private void validateDuplicateCpf(User user, String cpf, Long currentId) {
        boolean exists;
        if (currentId == null) {
            exists = contactRepository.existsByUserAndCpf(user, cpf);
        } else {
            exists = contactRepository.existsByUserAndCpfAndIdNot(user, cpf, currentId);
        }

        if (exists) {
            throw new RuntimeException("Já existe um contato com esse CPF para este usuário");
        }
    }

    private ContactResponse toResponse(Contact c) {
        return new ContactResponse(
                c.getId(),
                c.getName(),
                c.getCpf(),
                c.getPhone(),
                c.getCep(),
                c.getStreet(),
                c.getNumber(),
                c.getComplement(),
                c.getNeighborhood(),
                c.getCity(),
                c.getState(),
                c.getLatitude(),
                c.getLongitude()
        );
    }

}