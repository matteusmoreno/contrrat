package br.com.matteusmoreno.contrrat.contract.response;

import br.com.matteusmoreno.contrrat.contract.domain.Contract;
import br.com.matteusmoreno.contrrat.contract.constant.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ContractDetailsResponse(
        String id,
        String artistId,
        String customerId,
        List<String> availabilityIds,
        BigDecimal totalPrice,
        ContractStatus status,
        LocalDateTime createdAt) {

    public ContractDetailsResponse(Contract contract) {
        this(
                contract.getId(),
                contract.getArtistId(),
                contract.getCustomerId(),
                contract.getAvailabilityIds(),
                contract.getTotalPrice(),
                contract.getContractStatus(),
                contract.getCreatedAt());
    }
}
