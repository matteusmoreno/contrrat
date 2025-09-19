package br.com.matteusmoreno.contrrat.customer.service;

import br.com.matteusmoreno.contrrat.address.domain.Address;
import br.com.matteusmoreno.contrrat.address.service.AddressService;
import br.com.matteusmoreno.contrrat.artist.domain.Artist;
import br.com.matteusmoreno.contrrat.customer.domain.Customer;
import br.com.matteusmoreno.contrrat.customer.repository.CustomerRepository;
import br.com.matteusmoreno.contrrat.customer.request.CreateCustomerRequest;
import br.com.matteusmoreno.contrrat.customer.request.UpdateCustomerRequest;
import br.com.matteusmoreno.contrrat.exception.*;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import br.com.matteusmoreno.contrrat.user.constant.Profile;
import br.com.matteusmoreno.contrrat.user.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final AuthenticationService authenticationService; // Adicionado


    public CustomerService(CustomerRepository customerRepository, AddressService addressService, UserService userService, AuthenticationService authenticationService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) throw new EmailAlreadyExistsException("Email already registered");
        if (customerRepository.existsByPhoneNumber(request.phoneNumber())) throw new PhoneNumberAlreadyExistsException("Phone number already registered");

        Address address = addressService.createAddressObject(request.cep(), request.number(), request.complement());

        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .address(address)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();

        customerRepository.save(customer);
        createCustomerUser(request, customer);

        return customer;
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    @Transactional
    public void updateProfilePicture(String authenticatedCustomerId, String imageUrl) {
        Customer customer = getCustomerById(authenticatedCustomerId);

        customer.setProfilePictureUrl(imageUrl);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        String authenticatedCustomerId = authenticationService.getAuthenticatedCustomerId();
        if (!request.id().equals(authenticatedCustomerId)) throw new AccessDeniedException("You can only update your own customer profile.");

        Customer customer = getCustomerById(request.id());

        if (!customer.getEmail().equals(request.email()) && customerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        if (!customer.getPhoneNumber().equals(request.phoneNumber()) && customerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("Phone number already registered");
        }

        if (request.name() != null) customer.setName(request.name());
        if (request.birthDate() != null) customer.setBirthDate(request.birthDate());
        if (request.phoneNumber() != null) customer.setPhoneNumber(request.phoneNumber());
        if (request.email() != null) customer.setEmail(request.email());
        if (request.cep() != null) {
            Address address = addressService.createAddressObject(request.cep(), request.number(), request.complement());
            customer.setAddress(address);
        }
        if (request.number() != null) customer.getAddress().setNumber(request.number());
        if (request.complement() != null) customer.getAddress().setComplement(request.complement());

        customer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    @Transactional
    public void disableCustomer(String id) {
        String authenticatedCustomerId = authenticationService.getAuthenticatedCustomerId();
        if (!id.equals(authenticatedCustomerId)) throw new AccessDeniedException("You can only disable your own customer profile.");

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        if (!customer.getActive()) throw new CustomerAlreadyDisabledException("Customer is already disabled");

        customer.setActive(false);

        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Transactional
    public void enableCustomer(String id) {
        String authenticatedCustomerId = authenticationService.getAuthenticatedCustomerId();
        if (!id.equals(authenticatedCustomerId)) throw new AccessDeniedException("You can only enable your own customer profile.");

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        if (customer.getActive()) throw new CustomerAlreadyEnabledException("Customer is already enabled");

        customer.setActive(true);
        customer.setDeletedAt(null);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    private void createCustomerUser(CreateCustomerRequest request, Customer customer) {
        userService.createUser(request.name(), request.email(), Profile.CUSTOMER, null, customer.getId(), request.password());
    }
}
