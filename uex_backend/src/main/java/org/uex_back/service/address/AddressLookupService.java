package org.uex_back.service.address;

import org.uex_back.dto.address.AddressSuggestion;

import java.util.List;
import java.util.Optional;

/**
 * Interface responsável por padronizar serviços de busca de endereço.
 * Novas integrações com outras APIs de CEP podem ser adicionadas
 * simplesmente implementando este contrato.
 */

public interface AddressLookupService {

    List<AddressSuggestion> search(String uf, String city, String street);

    Optional<AddressSuggestion> findByCep(String cep);
}
