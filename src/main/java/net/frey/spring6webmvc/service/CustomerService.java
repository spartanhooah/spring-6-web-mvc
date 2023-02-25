package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();

    Optional<Customer> getCustomerById(UUID id);

    Customer addCustomer(Customer customer);

    void updateCustomer(UUID customerId, Customer customer);

    void delete(UUID customerId);
}
