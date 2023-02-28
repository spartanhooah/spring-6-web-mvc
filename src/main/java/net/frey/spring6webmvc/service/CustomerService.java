package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO addCustomer(CustomerDTO customer);

    void updateCustomer(UUID customerId, CustomerDTO customer);

    void delete(UUID customerId);
}
