package br.com.matteusmoreno.contrrat.availability.repository;

import br.com.matteusmoreno.contrrat.availability.domain.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    Page<Availability> findAllByArtistId(String artistId, Pageable pageable);

}
