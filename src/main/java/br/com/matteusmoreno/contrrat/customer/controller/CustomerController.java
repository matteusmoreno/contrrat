package br.com.matteusmoreno.contrrat.customer.controller;

import br.com.matteusmoreno.contrrat.customer.request.CreateCustomerRequest;
import br.com.matteusmoreno.contrrat.customer.domain.Customer;
import br.com.matteusmoreno.contrrat.customer.request.UpdateCustomerRequest;
import br.com.matteusmoreno.contrrat.customer.response.CustomerDetailsResponse;
import br.com.matteusmoreno.contrrat.customer.service.CustomerService;
import br.com.matteusmoreno.contrrat.security.AuthenticationService;
import br.com.matteusmoreno.contrrat.utils.UpdateProfilePictureRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    public CustomerController(CustomerService customerService, AuthenticationService authenticationService) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<CustomerDetailsResponse> create(@RequestBody @Valid CreateCustomerRequest request, UriComponentsBuilder uriBuilder) {
        Customer customer = customerService.createCustomer(request);
        var uri = uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new CustomerDetailsResponse(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsResponse> getById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);

        return ResponseEntity.ok(new CustomerDetailsResponse(customer));
    }

    @PatchMapping("/picture")
    public ResponseEntity<Void> updatePicture(@RequestBody UpdateProfilePictureRequest request) {
        String authenticatedCustomerId = authenticationService.getAuthenticatedCustomerId();
        customerService.updateProfilePicture(authenticatedCustomerId, request.profilePictureUrl());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDetailsResponse> update(@RequestBody @Valid UpdateCustomerRequest request) {
        Customer customer = customerService.updateCustomer(request);

        return ResponseEntity.ok(new CustomerDetailsResponse(customer));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable String id) {
        customerService.disableCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable String id) {
        customerService.enableCustomer(id);

        return ResponseEntity.noContent().build();
    }
}