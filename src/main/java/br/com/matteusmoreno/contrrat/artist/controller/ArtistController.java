package br.com.matteusmoreno.contrrat.artist.controller;

import br.com.matteusmoreno.contrrat.artist.constant.ArtisticField;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import br.com.matteusmoreno.contrrat.artist.request.CreateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.request.UpdateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.response.ArtistDetailsResponse;
import br.com.matteusmoreno.contrrat.artist.service.ArtistService;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import br.com.matteusmoreno.contrrat.utils.UpdateProfilePictureRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;
    private final AuthenticationService authenticationService;


    public ArtistController(ArtistService artistService, AuthenticationService authenticationService) {
        this.artistService = artistService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<ArtistDetailsResponse> create(@RequestBody @Valid CreateArtistRequest request, UriComponentsBuilder uriBuilder) {
        Artist artist = artistService.createArtist(request);
        URI uri = uriBuilder.path("/artists/{id}").buildAndExpand(artist.getId()).toUri();

        return ResponseEntity.created(uri).body(new ArtistDetailsResponse(artist));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDetailsResponse> getById(@PathVariable String id) {
        Artist artist = artistService.getArtistById(id);

        return ResponseEntity.ok(new ArtistDetailsResponse(artist));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ArtistDetailsResponse>> getAll(Pageable pageable) {
        Page<ArtistDetailsResponse> artists = artistService.getAllArtists(pageable);

        return ResponseEntity.ok(artists);
    }

    @GetMapping("/artists-by-field/{artisticField}")
    public ResponseEntity<Page<ArtistDetailsResponse>> getByArtisticField(@PathVariable ArtisticField artisticField, Pageable pageable) {
        Page<ArtistDetailsResponse> artists = artistService.getArtistsByArtisticField(artisticField, pageable);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/premium-artists")
    public ResponseEntity<Page<ArtistDetailsResponse>> getPremiumArtists(Pageable pageable) {
        Page<ArtistDetailsResponse> artists = artistService.getArtistsByPremium(pageable);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/all-active")
    public ResponseEntity<Page<ArtistDetailsResponse>> getAllActive(Pageable pageable) {
        Page<ArtistDetailsResponse> artists = artistService.getAllActiveArtists(pageable);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/artistic-fields")
    public ResponseEntity<List<Map<String, String>>> getArtisticFields() {
        List<Map<String, String>> fields = artistService.getArtisticFields();
        return ResponseEntity.ok(fields);
    }

    @PatchMapping("/picture")
    public ResponseEntity<Void> updatePicture(@RequestBody UpdateProfilePictureRequest request) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        artistService.updateProfilePicture(authenticatedArtistId, request.profilePictureUrl());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<ArtistDetailsResponse> update(@RequestBody @Valid UpdateArtistRequest request) {
        Artist artist = artistService.updateArtist(request);

        return ResponseEntity.ok(new ArtistDetailsResponse(artist));
    }

    @PatchMapping("/promote/{id}")
    public ResponseEntity<Void> promoteToPremium(@PathVariable String id) {
        artistService.upgradeToPremium(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artistService.disableArtist(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable String id) {
        artistService.enableArtist(id);

        return ResponseEntity.noContent().build();
    }
}
