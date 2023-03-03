package net.frey.spring6webmvc.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.frey.spring6webmvc.model.dto.CustomerDTO;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO addCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);

    boolean delete(UUID customerId);

    void patchCustomer(UUID customerId, CustomerDTO customer);
}
