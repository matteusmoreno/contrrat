package br.com.matteusmoreno.contrrat.artist.response;

import br.com.matteusmoreno.contrrat.address.response.AddressDetailsResponse;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;

import java.time.LocalDateTime;

public record ArtistDetailsResponse(
    String id,
    String name,
    String birthDate,
    String phoneNumber,
    String email,
    String description,
    AddressDetailsResponse address,
    String profilePictureUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Boolean active) {

    public ArtistDetailsResponse(Artist artist) {
        this(
            artist.getId(),
            artist.getName(),
            artist.getBirthDate().toString(),
            artist.getPhoneNumber(),
            artist.getEmail(),
            artist.getDescription(),
            new AddressDetailsResponse(artist.getAddress()),
            artist.getProfilePictureUrl(),
            artist.getCreatedAt(),
            artist.getUpdatedAt(),
            artist.getDeletedAt(),
            artist.getActive()
        );
    }
}
