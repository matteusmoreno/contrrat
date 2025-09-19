package br.com.matteusmoreno.contrrat.artist.domain;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "artists")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Artist {

    @Id
    private String id;
    private String name;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String description;
    private Address address;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean active;
}
