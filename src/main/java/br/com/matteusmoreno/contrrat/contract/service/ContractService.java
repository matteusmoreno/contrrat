package br.com.matteusmoreno.contrrat.contract.service;

import br.com.matteusmoreno.contrrat.availability.constant.AvailabilityStatus;
import br.com.matteusmoreno.contrrat.availability.domain.Availability;
import br.com.matteusmoreno.contrrat.availability.service.AvailabilityService;
import br.com.matteusmoreno.contrrat.contract.domain.Contract;
import br.com.matteusmoreno.contrrat.contract.repository.ContractRepository;
import br.com.matteusmoreno.contrrat.contract.constant.ContractStatus;
import br.com.matteusmoreno.contrrat.contract.request.CreateContractRequest;
import br.com.matteusmoreno.contrrat.exception.ContractNotFoundException;
import br.com.matteusmoreno.contrrat.exception.InvalidContractStatusException;
import br.com.matteusmoreno.contrrat.exception.MismatchedArtistsException;
import br.com.matteusmoreno.contrrat.exception.SlotsNotAvailableException;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final AvailabilityService availabilityService;
    private final AuthenticationService authenticationService;

    public ContractService(ContractRepository contractRepository, AvailabilityService availabilityService, AuthenticationService authenticationService) {
        this.contractRepository = contractRepository;
        this.availabilityService = availabilityService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Contract createContract(CreateContractRequest request) {
        String customerId = authenticationService.getAuthenticatedCustomerId();

        List<Availability> availabilities = request.availabilityIds().stream()
                .map(availabilityService::getAvailabilityById)
                .toList();

        validateAvailabilities(availabilities);

        String artistId = availabilities.getFirst().getArtistId();

        BigDecimal totalPrice = availabilities.stream()
                .map(Availability::getPrice)
                .reduce(BigDecimal::add)
                .orElseThrow();

        Contract contract = Contract.builder()
                .id(UUID.randomUUID().toString())
                .artistId(artistId)
                .customerId(customerId)
                .availabilityIds(request.availabilityIds())
                .totalPrice(totalPrice)
                .contractStatus(ContractStatus.PENDING_CONFIRMATION)
                .createdAt(LocalDateTime.now())
                .build();

        availabilities.forEach(availability ->
                availabilityService.changeAvailabilityStatus(availability.getId(), AvailabilityStatus.UNAVAILABLE));

        return contractRepository.save(contract);
    }

    @Transactional
    public void confirmContract(String contractId) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new ContractNotFoundException("Contract not found"));

        if (!contract.getArtistId().equals(authenticatedArtistId)) throw new SecurityException("User is not authorized to confirm this contract.");
        if (contract.getContractStatus() != ContractStatus.PENDING_CONFIRMATION) throw new InvalidContractStatusException("Contract cannot be confirmed as it is not pending confirmation.");

        contract.setContractStatus(ContractStatus.CONFIRMED);
        contract.setConfirmedAt(LocalDateTime.now());

        // Marca TODAS as agendas como "BOOKED"
        contract.getAvailabilityIds().forEach(availabilityId ->
                availabilityService.changeAvailabilityStatus(availabilityId, AvailabilityStatus.BOOKED));

        contractRepository.save(contract);
    }

    @Transactional
    public void rejectContract(String contractId) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new ContractNotFoundException("Contract not found"));

        if (!contract.getArtistId().equals(authenticatedArtistId)) throw new SecurityException("User is not authorized to reject this contract.");
        if (contract.getContractStatus() != ContractStatus.PENDING_CONFIRMATION) throw new InvalidContractStatusException("Contract cannot be rejected as it is not pending confirmation.");

        contract.setContractStatus(ContractStatus.REJECTED);
        contract.setRejectedAt(LocalDateTime.now());

        // Libera TODAS as agendas novamente
        contract.getAvailabilityIds().forEach(availabilityId ->
                availabilityService.changeAvailabilityStatus(availabilityId, AvailabilityStatus.AVAILABLE));

        contractRepository.save(contract);
    }






    // MÉTODOS PRIVADOS
    private void validateAvailabilities(List<Availability> availabilities) {
        if (availabilities.stream().map(Availability::getArtistId).distinct().count() > 1) {
            throw new MismatchedArtistsException("All availability slots must belong to the same artist.");
        }
        if (availabilities.stream().anyMatch(a -> a.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE)) {
            throw new SlotsNotAvailableException("One or more availability slots are not available for booking.");
        }
    }
}
