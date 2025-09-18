package br.com.matteusmoreno.contrrat.customer.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateCustomerRequest(
        @NotBlank(message = "Customer ID is required")
        String id,
        String name,
        LocalDate birthDate,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{4,5}-\\d{4}", message = "Phone number must be in the format (xx)xxxxx-xxxx")
        String phoneNumber,
        @Email(message = "Email must be valid")
        String email,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must be in the format xxxxx-xxx")
        String cep,
        String number,
        String complement) {}
