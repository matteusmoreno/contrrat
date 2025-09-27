package br.com.matteusmoreno.contrrat.customer.response;

import br.com.matteusmoreno.contrrat.customer.domain.Customer;

public record CustomerSummaryResponse(
        String id,
        String name,
        String profilePictureUrl) {

    public CustomerSummaryResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getName(),
                customer.getProfilePictureUrl());
    }
}