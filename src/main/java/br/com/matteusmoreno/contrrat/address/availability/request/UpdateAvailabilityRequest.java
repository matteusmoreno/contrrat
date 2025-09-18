package br.com.matteusmoreno.contrrat.address.availability.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateAvailabilityRequest(
        @NotBlank(message = "Availability ID is required")
        String id,
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,
        @NotNull(message = "End time is required")
        LocalDateTime endTime) {}
