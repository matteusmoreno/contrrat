package br.com.matteusmoreno.contrrat.address.response;

import br.com.matteusmoreno.contrrat.address.domain.Address;

public record AddressDetailsResponse(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode) {

    public AddressDetailsResponse(Address address) {
        this(
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode());
    }
}
