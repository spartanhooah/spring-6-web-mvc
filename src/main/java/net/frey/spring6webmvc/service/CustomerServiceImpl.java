package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
            .id(randomUUID())
            .name("Billie Jean")
            .version(1)
            .created(now())
            .lastModified(now())
            .build();

        Customer customer2 = Customer.builder()
            .id(randomUUID())
            .name("Jim Bob")
            .version(1)
            .created(now())
            .lastModified(now())
            .build();

        customerMap = Map.of(customer1.getId(), customer1, customer2.getId(), customer2);
    }


    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerMap.get(id);
    }
}