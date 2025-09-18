package br.com.matteusmoreno.contrrat.artist.repository;

import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
