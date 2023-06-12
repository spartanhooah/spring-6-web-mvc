package net.frey.spring6webmvc.service;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static java.util.UUID.randomUUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.frey.spring6webmvc.model.dto.CustomerDTO;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(randomUUID())
                .name("Billie Jean")
                .version(1)
                .createdDate(now())
                .lastModified(now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(randomUUID())
                .name("Jim Bob")
                .version(1)
                .createdDate(now())
                .lastModified(now())
                .build();

        customerMap = new HashMap<>(of(customer1.getId(), customer1, customer2.getId(), customer2));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(randomUUID())
                .createdDate(now())
                .lastModified(now())
                .name(customer.getName())
                .version(customer.getVersion() == 0 ? 1 : customer.getVersion())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(customerId);

        existing.setName(customer.getName());
        existing.setLastModified(now());

        return Optional.of(existing);
    }

    @Override
    public boolean delete(UUID customerId) {
        customerMap.remove(customerId);

        return true;
    }

    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {}
}
