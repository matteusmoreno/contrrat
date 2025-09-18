package br.com.matteusmoreno.contrrat.address.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequest(
        @NotBlank(message = "Street cannot be blank")
        String street,
        String number,
        String complement,
        @NotBlank(message = "Neighborhood cannot be blank")
        String neighborhood,
        @NotBlank(message = "City cannot be blank")
        String city,
        @NotBlank(message = "State cannot be blank")
        String state,
        @NotBlank(message = "Zip code cannot be blank")
        String zipCode) {
}
