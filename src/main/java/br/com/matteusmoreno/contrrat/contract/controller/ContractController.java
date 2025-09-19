package br.com.matteusmoreno.contrrat.contract.controller;

import br.com.matteusmoreno.contrrat.contract.domain.Contract;
import br.com.matteusmoreno.contrrat.contract.service.ContractService;
import br.com.matteusmoreno.contrrat.contract.request.CreateContractRequest;
import br.com.matteusmoreno.contrrat.contract.response.ContractDetailsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractDetailsResponse> create(@RequestBody @Valid CreateContractRequest request, UriComponentsBuilder uriBuilder) {
        Contract contract = contractService.createContract(request);
        URI uri = uriBuilder.path("/contracts/{id}").buildAndExpand(contract.getId()).toUri();

        return ResponseEntity.created(uri).body(new ContractDetailsResponse(contract));
    }

    @GetMapping("/my-contracts-as-customer")
    public ResponseEntity<Page<ContractDetailsResponse>> getMyContractsAsCustomer(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<ContractDetailsResponse> contracts = contractService.getContractsForAuthenticatedCustomer(pageable);
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/my-contracts-as-artist")
    public ResponseEntity<Page<ContractDetailsResponse>> getMyContractsAsArtist(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<ContractDetailsResponse> contracts = contractService.getContractsForAuthenticatedArtist(pageable);
        return ResponseEntity.ok(contracts);
    }

    @PatchMapping("/confirm/{contractId}")
    public ResponseEntity<Void> confirmContract(@PathVariable String contractId) {
        contractService.confirmContract(contractId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reject/{contractId}")
    public ResponseEntity<Void> rejectContract(@PathVariable String contractId) {
        contractService.rejectContract(contractId);

        return ResponseEntity.noContent().build();
    }
}
