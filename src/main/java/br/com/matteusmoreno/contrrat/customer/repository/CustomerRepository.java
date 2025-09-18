package br.com.matteusmoreno.contrrat.customer.repository;

import br.com.matteusmoreno.contrrat.customer.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
