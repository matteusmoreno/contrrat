package br.com.matteusmoreno.contrrat.artist.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateArtistRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Birth date is required")
        LocalDate birthDate,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "\\(\\d{2}\\)\\d{4,5}-\\d{4}", message = "Phone number must be in the format (xx)xxxxx-xxxx")
        String phoneNumber,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Password is required")
        String password,
        String description,
        String profilePictureUrl,
        @NotBlank(message = "CEP is required")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must be in the format xxxxx-xxx")
        String cep,
        String number,
        String complement) {}
