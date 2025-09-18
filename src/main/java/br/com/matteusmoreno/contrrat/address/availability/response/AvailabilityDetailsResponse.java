package br.com.matteusmoreno.contrrat.address.availability.response;

import br.com.matteusmoreno.contrrat.address.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.address.availability.domain.Availability;

import java.time.LocalDateTime;

public record AvailabilityDetailsResponse(
        String id,
        String artistId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AvailabilityStatus availabilityStatus
) {
    public AvailabilityDetailsResponse(Availability availability) {
        this(
                availability.getId(),
                availability.getArtistId(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.getAvailabilityStatus()
        );
    }
}
