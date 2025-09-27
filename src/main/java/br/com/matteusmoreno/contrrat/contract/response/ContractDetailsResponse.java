package br.com.matteusmoreno.contrrat.contract.response;

import br.com.matteusmoreno.contrrat.artist.response.ArtistSummaryResponse;
import br.com.matteusmoreno.contrrat.contract.constant.ContractStatus;
import br.com.matteusmoreno.contrrat.contract.domain.Contract;
import br.com.matteusmoreno.contrrat.customer.response.CustomerSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ContractDetailsResponse(
        String id,
        ArtistSummaryResponse artist,
        CustomerSummaryResponse customer,
        List<String> availabilityIds,
        BigDecimal totalPrice,
        ContractStatus status,
        LocalDateTime createdAt) {

    public ContractDetailsResponse(Contract contract, ArtistSummaryResponse artist, CustomerSummaryResponse customer) {
        this(
                contract.getId(),
                artist,
                customer,
                contract.getAvailabilityIds(),
                contract.getTotalPrice(),
                contract.getContractStatus(),
                contract.getCreatedAt());
    }
}