package br.com.matteusmoreno.contrrat.artist.service;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import br.com.matteusmoreno.contrrat.address.service.AddressService;
import br.com.matteusmoreno.contrrat.artist.constant.ArtisticField;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import br.com.matteusmoreno.contrrat.artist.repository.ArtistRepository;
import br.com.matteusmoreno.contrrat.artist.request.CreateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.request.UpdateArtistRequest;
import br.com.matteusmoreno.contrrat.artist.response.ArtistDetailsResponse;
import br.com.matteusmoreno.contrrat.exception.*;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import br.com.matteusmoreno.contrrat.user.constant.Profile;
import br.com.matteusmoreno.contrrat.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public ArtistService(ArtistRepository artistRepository, AddressService addressService, UserService userService, AuthenticationService authenticationService) {
        this.artistRepository = artistRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Artist createArtist(CreateArtistRequest request) {
        if (artistRepository.existsByEmail(request.email())) throw new EmailAlreadyExistsException("Email already registered");
        if (artistRepository.existsByPhoneNumber(request.phoneNumber())) throw new PhoneNumberAlreadyExistsException("Phone number already registered");

        Address address = addressService.createAddressObject(request.cep(), request.number(), request.complement());

        Artist artist = Artist.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .artisticField(request.artisticField())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .description(request.description())
                .profilePictureUrl(request.profilePictureUrl())
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

    public Page<ArtistDetailsResponse> getAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable).map(ArtistDetailsResponse::new);
    }

    public List<Map<String, String>> getArtisticFields() {
        return Arrays.stream(ArtisticField.values())
                .map(field -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", field.name());
                    map.put("displayName", field.getDisplayName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProfilePicture(String authenticatedArtistId, String imageUrl) {
        Artist artist = getArtistById(authenticatedArtistId);

        artist.setProfilePictureUrl(imageUrl);
        artist.setUpdatedAt(LocalDateTime.now());

        artistRepository.save(artist);
    }

    @Transactional
    public Artist updateArtist(UpdateArtistRequest request) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!request.id().equals(authenticatedArtistId)) throw new AccessDeniedException("You can only update your own artist profile.");

        Artist artist = getArtistById(request.id());

        if (!artist.getEmail().equals(request.email()) && artistRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (!artist.getPhoneNumber().equals(request.phoneNumber()) && artistRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already registered");
        }

        if (request.name() != null) artist.setName(request.name());
        if (request.artisticField() != null) artist.setArtisticField(request.artisticField());
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
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!id.equals(authenticatedArtistId)) throw new AccessDeniedException("You can only disable your own artist profile.");

        Artist artist = getArtistById(id);

        if (!artist.getActive()) throw new ArtistAlreadyDisabledException("Artist is already disabled");

        artist.setActive(false);
        artist.setDeletedAt(LocalDateTime.now());

        artistRepository.save(artist);
    }

    @Transactional
    public void enableArtist(String id) {
        String authenticatedArtistId = authenticationService.getAuthenticatedArtistId();
        if (!id.equals(authenticatedArtistId)) throw new AccessDeniedException("You can only enable your own artist profile.");

        Artist artist = getArtistById(id);

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
