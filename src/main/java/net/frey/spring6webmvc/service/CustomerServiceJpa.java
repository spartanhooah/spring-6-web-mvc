package net.frey.spring6webmvc.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.mapper.CustomerMapper;
import net.frey.spring6webmvc.model.dto.CustomerDTO;
import net.frey.spring6webmvc.repository.CustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(mapper.entityToDto(repository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customer) {
        return mapper.entityToDto(repository.save(mapper.dtoToEntity(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        repository
                .findById(customerId)
                .ifPresentOrElse(
                        foundCustomer -> {
                            foundCustomer.setName(customer.getName());

                            atomicReference.set(Optional.of(mapper.entityToDto(repository.save(foundCustomer))));
                        },
                        () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public boolean delete(UUID customerId) {
        if (repository.existsById(customerId)) {
            repository.deleteById(customerId);

            return true;
        }

        return false;
    }

    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {
        repository
                .findById(customerId)
                .ifPresentOrElse(
                        foundCustomer -> {
                            if (customer.getName() != null) {
                                foundCustomer.setName(customer.getName());
                            }

                            repository.save(foundCustomer);
                        },
                        () -> {
                            throw new NotFoundException();
                        });
    }
}
