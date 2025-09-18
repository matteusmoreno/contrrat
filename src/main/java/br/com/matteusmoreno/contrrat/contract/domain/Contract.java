package br.com.matteusmoreno.contrrat.contract.domain;

import br.com.matteusmoreno.contrrat.contract.constant.ContractStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "contracts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Contract {

    @Id
    private String id;
    private String artistId;
    private String customerId;
    private List<String> availabilityIds;
    private BigDecimal totalPrice;
    private ContractStatus contractStatus;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime rejectedAt;
}
