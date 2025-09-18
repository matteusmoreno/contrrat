package br.com.matteusmoreno.contrrat.contract.repository;

import br.com.matteusmoreno.contrrat.contract.domain.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractRepository extends MongoRepository<Contract, String> {
}
