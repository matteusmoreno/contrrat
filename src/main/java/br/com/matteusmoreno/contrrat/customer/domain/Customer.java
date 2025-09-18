package br.com.matteusmoreno.contrrat.customer.domain;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Customer {

    @Id
    private String id;
    private String name;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean active;
}
