package br.com.matteusmoreno.contrrat.artist.service;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import br.com.matteusmoreno.contrrat.address.service.AddressService;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import br.com.matteusmoreno.contrrat.artist.repository.ArtistRepository;
import br.com.matteusmoreno.contrrat.artist.request.CreateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.request.UpdateArtistRequest;
import br.com.matteusmoreno.contrrat.exception.*;
import br.com.matteusmoreno.contrrat.user.Profile;
import br.com.matteusmoreno.contrrat.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final AddressService addressService;
    private final UserService userService;

    public ArtistService(ArtistRepository artistRepository, AddressService addressService, UserService userService) {
        this.artistRepository = artistRepository;
        this.addressService = addressService;
        this.userService = userService;
    }

    @Transactional
    public Artist createArtist(CreateArtistRequest request) {
        if (artistRepository.existsByEmail(request.email())) throw new EmailAlreadyExistsException("Email already registered");
        if (artistRepository.existsByPhoneNumber(request.phoneNumber())) throw new PhoneNumberAlreadyExistsException("Phone number already registered");

        Address address = addressService.createAddressObject(request.cep(), request.number(), request.complement());

        Artist artist = Artist.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .description(request.description())
                .address(address)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();

        artistRepository.save(artist);
        createArtistUser(request, artist);

        return artist;
    }

    public Artist getArtistById(String id) {
        return artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + id));
    }

    @Transactional
    public Artist updateArtist(UpdateArtistRequest request) {
        Artist artist = artistRepository.findById(request.id()).orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + request.id()));

        if (!artist.getEmail().equals(request.email()) && artistRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (!artist.getPhoneNumber().equals(request.phoneNumber()) && artistRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already registered");
        }

        if (request.name() != null) artist.setName(request.name());
        if (request.birthDate() != null) artist.setBirthDate(request.birthDate());
        if (request.phoneNumber() != null) artist.setPhoneNumber(request.phoneNumber());
        if (request.email() != null) artist.setEmail(request.email());
        if (request.description() != null) artist.setDescription(request.description());

        if (request.cep() != null) {
            Address updatedAddress = addressService.createAddressObject(request.cep(), request.number(), request.complement());
            artist.setAddress(updatedAddress);
        }

        if (request.number() != null) artist.getAddress().setNumber(request.number());
        if (request.complement() != null) artist.getAddress().setComplement(request.complement());

        artist.setUpdatedAt(LocalDateTime.now());

        return artistRepository.save(artist);
    }

    @Transactional
    public void disableArtist(String id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + id));

        if (!artist.getActive()) throw new ArtistAlreadyDisabledException("Artist is already disabled");

        artist.setActive(false);
        artist.setDeletedAt(LocalDateTime.now());

        artistRepository.save(artist);
    }

    @Transactional
    public void enableArtist(String id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Artist not found with id: " + id));

        if (artist.getActive()) throw new ArtistAlreadyEnabledException("Artist is already enabled");

        artist.setActive(true);
        artist.setDeletedAt(null);
        artist.setUpdatedAt(LocalDateTime.now());

        artistRepository.save(artist);
    }

    private void createArtistUser(CreateArtistRequest request, Artist artist) {
        userService.createUser(request.name(), request.email(), Profile.ARTIST, artist.getId(), null, request.password());
    }
}
