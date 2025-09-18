package br.com.matteusmoreno.contrrat.availability.service;

import br.com.matteusmoreno.contrrat.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.availability.domain.Availability;
import br.com.matteusmoreno.contrrat.availability.repository.AvailabilityRepository;
import br.com.matteusmoreno.contrrat.availability.request.CreateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.availability.request.UpdateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.availability.response.AvailabilityDetailsResponse;
import br.com.matteusmoreno.contrrat.exception.InvalidTimeRangeException;
import br.com.matteusmoreno.contrrat.exception.RedundantStatusChangeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Transactional
    public Availability createAvailability(CreateAvailabilityRequest request) {
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
        if (request.endTime().isBefore(request.startTime())) throw new InvalidTimeRangeException("End time cannot be before start time");
        if (request.startTime().isBefore(LocalDateTime.now()) || request.endTime().isBefore(LocalDateTime.now())) throw new InvalidTimeRangeException("Availability times must be in the future");

        Availability availability = getAvailabilityById(request.id());

        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());
        if (request.price() != null) availability.setPrice(request.price());

        return availabilityRepository.save(availability);
    }

    @Transactional
    public void changeAvailabilityStatus(String id, AvailabilityStatus status) {
        Availability availability = getAvailabilityById(id);

        if (availability.getAvailabilityStatus() == status) throw new RedundantStatusChangeException("Availability is already " + status);

        availability.setAvailabilityStatus(status);
        availabilityRepository.save(availability);
    }

}
