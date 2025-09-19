package br.com.matteusmoreno.contrrat.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank(message = "Email is required")
        String name,
        @NotBlank(message = "Name is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotNull(message = "Profile is required")
        Profile profile,
        String artistId,
        String customerId
) {}
