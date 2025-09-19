package br.com.matteusmoreno.contrrat.artist.controller;

import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import br.com.matteusmoreno.contrrat.artist.request.CreateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.request.UpdateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.response.ArtistDetailsResponse;
import br.com.matteusmoreno.contrrat.artist.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
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

    @PutMapping("/update")
    public ResponseEntity<ArtistDetailsResponse> update(@RequestBody @Valid UpdateArtistRequest request) {
        Artist artist = artistService.updateArtist(request);

        return ResponseEntity.ok(new ArtistDetailsResponse(artist));
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
