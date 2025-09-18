package br.com.matteusmoreno.contrrat.contract.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateContractRequest(
        @NotBlank(message = "Customer ID is required")
        String customerId,
        @NotEmpty(message = "At least one Availability ID is required")
        List<String> availabilityIds) {
}
