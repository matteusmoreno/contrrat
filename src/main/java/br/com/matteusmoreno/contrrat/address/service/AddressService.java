package br.com.matteusmoreno.contrrat.address.service;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import br.com.matteusmoreno.contrrat.client.ViaCepClient;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final ViaCepClient viaCepClient;

    public AddressService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Address createAddressObject(String zipCode, String addressNumber, String complement) {
        var viaCepResponse = viaCepClient.getAddressByZipcode(zipCode);
        return Address.builder()
                .zipCode(viaCepResponse.cep())
                .street(viaCepResponse.logradouro())
                .number(addressNumber)
                .complement(complement)
                .neighborhood(viaCepResponse.bairro())
                .city(viaCepResponse.localidade())
                .state(viaCepResponse.uf())
                .build();
    }
}
