package br.com.matteusmoreno.contrrat.availability.response;

import br.com.matteusmoreno.contrrat.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.availability.domain.Availability;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AvailabilityDetailsResponse(
        String id,
        String artistId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AvailabilityStatus availabilityStatus,
        BigDecimal price
) {
    public AvailabilityDetailsResponse(Availability availability) {
        this(
                availability.getId(),
                availability.getArtistId(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.getAvailabilityStatus(),
                availability.getPrice()
        );
    }
}
