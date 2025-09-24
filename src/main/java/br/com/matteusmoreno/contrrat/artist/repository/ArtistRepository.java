package br.com.matteusmoreno.contrrat.artist.repository;

import br.com.matteusmoreno.contrrat.artist.constant.ArtisticField;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import com.mongodb.client.MongoIterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.validation.ObjectError;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<Artist> findAllByArtisticField(ArtisticField artisticField, Pageable pageable);

    Page<Artist> findAllByPremiumIsTrue(Pageable pageable);

    Page<Artist> findAllByActiveTrue(Pageable pageable);
}
