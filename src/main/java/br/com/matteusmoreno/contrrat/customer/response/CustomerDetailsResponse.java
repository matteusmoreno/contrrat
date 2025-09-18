package br.com.matteusmoreno.contrrat.customer.response;

import br.com.matteusmoreno.contrrat.address.response.AddressDetailsResponse;
import br.com.matteusmoreno.contrrat.customer.domain.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerDetailsResponse(
        String id,
        String name,
        LocalDate birthDate,
        String phoneNumber,
        String email,
        AddressDetailsResponse address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public CustomerDetailsResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getName(),
                customer.getBirthDate(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                new AddressDetailsResponse(customer.getAddress()),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getActive()
        );
    }
}
