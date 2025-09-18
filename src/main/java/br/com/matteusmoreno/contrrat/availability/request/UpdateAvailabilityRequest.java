package br.com.matteusmoreno.contrrat.availability.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateAvailabilityRequest(
        @NotBlank(message = "Availability ID is required")
        String id,
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,
        @NotNull(message = "End time is required")
        LocalDateTime endTime,
        @Positive(message = "Price must be positive")
        BigDecimal price) {}
