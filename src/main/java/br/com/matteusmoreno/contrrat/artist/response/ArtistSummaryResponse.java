package br.com.matteusmoreno.contrrat.artist.response;

import br.com.matteusmoreno.contrrat.artist.domain.Artist;

public record ArtistSummaryResponse(
        String id,
        String name,
        String artisticField,
        String profilePictureUrl) {

    public ArtistSummaryResponse(Artist artist) {
        this(
                artist.getId(),
                artist.getName(),
                artist.getArtisticField() != null ? artist.getArtisticField().getDisplayName() : "N/A",
                artist.getProfilePictureUrl());
    }
}