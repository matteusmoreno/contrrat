package br.com.matteusmoreno.contrrat.address.availability.repository;

import br.com.matteusmoreno.contrrat.address.availability.domain.Availability;
import com.mongodb.client.MongoIterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    Page<Availability> findAllByArtistId(String artistId, Pageable pageable);

}
