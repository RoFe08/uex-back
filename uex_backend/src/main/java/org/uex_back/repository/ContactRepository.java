package org.uex_back.repository;


import org.uex_back.model.Contact;
import org.uex_back.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("""
           SELECT c
           FROM Contact c
           WHERE c.user = :user
             AND (
                    :term IS NULL
                 OR :term = ''
                 OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                 OR c.cpf LIKE CONCAT('%', :term, '%')
             )
           """)
    Page<Contact> searchByUserAndTerm(User user, String term, Pageable pageable);

    Optional<Contact> findByIdAndUser(Long id, User user);

    boolean existsByUserAndCpf(User user, String cpf);

    boolean existsByUserAndCpfAndIdNot(User user, String cpf, Long id);
}
