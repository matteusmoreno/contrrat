package br.com.matteusmoreno.contrrat.address.availability.controller;

import br.com.matteusmoreno.contrrat.address.availability.service.AvailabilityService;
import br.com.matteusmoreno.contrrat.address.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.address.availability.domain.Availability;
import br.com.matteusmoreno.contrrat.address.availability.request.CreateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.address.availability.request.UpdateAvailabilityRequest;
import br.com.matteusmoreno.contrrat.address.availability.response.AvailabilityDetailsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<AvailabilityDetailsResponse> create(@RequestBody @Valid CreateAvailabilityRequest request, UriComponentsBuilder uriBuilder) {
        Availability availability = availabilityService.createAvailability(request);
        var uri = uriBuilder.path("/availability/{id}").buildAndExpand(availability.getId()).toUri();

        return ResponseEntity.created(uri).body(new AvailabilityDetailsResponse(availability));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityDetailsResponse> getById(@PathVariable String id) {
        Availability availability = availabilityService.getAvailabilityById(id);

        return ResponseEntity.ok(new AvailabilityDetailsResponse(availability));
    }

    @GetMapping("/get-all-by-artist/{artistId}")
    public ResponseEntity<?> getAllByArtistId(@PathVariable String artistId, @PageableDefault(size = 15, sort = {"startTime"}) Pageable pageable) {
        var page = availabilityService.getAllAvailabilityByArtistId(artistId, pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<AvailabilityDetailsResponse> update(@RequestBody @Valid UpdateAvailabilityRequest request) {
        Availability availability = availabilityService.updateAvailability(request);

        return ResponseEntity.ok(new AvailabilityDetailsResponse(availability));
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable String id, @RequestParam AvailabilityStatus status) {
        availabilityService.changeAvailabilityStatus(id, status);

        return ResponseEntity.noContent().build();
    }
}
