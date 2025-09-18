package br.com.matteusmoreno.contrrat.address.availability.domain;

import br.com.matteusmoreno.contrrat.address.availability.constant.AvailabilityStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "availability")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Availability {

    @Id
    private String id;
    private String artistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AvailabilityStatus availabilityStatus;
}
