package br.com.matteusmoreno.contrrat.availability.service;

import br.com.matteusmoreno.contrrat.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.availability.domain.Availability;
import br.com.matteusmoreno.contrrat.availability.repository.AvailabilityRepository;
import br.com.matteusmoreno.contrrat.availability.request.CreateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.availability.request.UpdateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.availability.response.AvailabilityDetailsResponse;
import br.com.matteusmoreno.contrrat.exception.InvalidTimeRangeException;
import br.com.matteusmoreno.contrrat.exception.RedundantStatusChangeException;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AuthenticationService authenticationService;


    public AvailabilityService(AvailabilityRepository availabilityRepository, AuthenticationService authenticationService) {
        this.availabilityRepository = availabilityRepository;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Availability createAvailability(CreateAvailabilityRequest request) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!request.artistId().equals(authenticatedArtistId)) throw new AccessDeniedException("You can only create availability for your own artist profile.");

        if (request.endTime().isBefore(request.startTime())) throw new InvalidTimeRangeException("End time cannot be before start time");
        if (request.startTime().isBefore(LocalDateTime.now()) || request.endTime().isBefore(LocalDateTime.now())) throw new InvalidTimeRangeException("Availability times must be in the future");

        Availability availability = Availability.builder()
                .id(UUID.randomUUID().toString())
                .artistId(request.artistId())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .availabilityStatus(request.availabilityStatus())
                .price(request.price())
                .build();

        return availabilityRepository.save(availability);
    }

    public Availability getAvailabilityById(String id) {
        return availabilityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Availability not found with id: " + id));
    }

    public Page<AvailabilityDetailsResponse> getAllAvailabilityByArtistId(String artistId, Pageable pageable) {
        return availabilityRepository.findAllByArtistId(artistId, pageable).map(AvailabilityDetailsResponse::new);
    }

    @Transactional
    public Availability updateAvailability(UpdateAvailabilityRequest request) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        Availability availability = getAvailabilityById(request.id());

        if (!availability.getArtistId().equals(authenticatedArtistId)) {
            throw new AccessDeniedException("User is not authorized to update this availability.");
        }

        if (request.startTime() != null && request.endTime() != null) {
            if (request.endTime().isBefore(request.startTime())) {
                throw new InvalidTimeRangeException("End time cannot be before start time");
            }
            if (request.startTime().isBefore(LocalDateTime.now()) || request.endTime().isBefore(LocalDateTime.now())) {
                throw new InvalidTimeRangeException("Availability times must be in the future");
            }
        }

        if (request.startTime() != null) availability.setStartTime(request.startTime());
        if (request.endTime() != null) availability.setEndTime(request.endTime());
        if (request.availabilityStatus() != null) availability.setAvailabilityStatus(request.availabilityStatus());

        if (request.availabilityStatus() == AvailabilityStatus.AVAILABLE && (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("Price must be a positive value for available slots.");
        }

        availability.setPrice(request.price());

        return availabilityRepository.save(availability);
    }

    @Transactional
    public void deleteAvailability(String id) {
        Availability availability = getAvailabilityById(id);
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!availability.getArtistId().equals(authenticatedArtistId)) throw new AccessDeniedException("You can only delete your own availability.");

        if (availability.getAvailabilityStatus() == AvailabilityStatus.BOOKED) {
            throw new IllegalStateException("Cannot delete a booked availability.");
        }

        availabilityRepository.deleteById(id);
    }

    @Transactional
    public void changeAvailabilityStatus(String id, AvailabilityStatus status) {
        Availability availability = getAvailabilityById(id);

        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!availability.getArtistId().equals(authenticatedArtistId)) throw new AccessDeniedException("User is not authorized to update this availability.");

        if (availability.getAvailabilityStatus() == status) throw new RedundantStatusChangeException("Availability is already " + status);

        availability.setAvailabilityStatus(status);
        availabilityRepository.save(availability);
    }

}
