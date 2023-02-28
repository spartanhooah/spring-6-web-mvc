package net.frey.spring6webmvc.controller;

import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.model.dto.CustomerDTO;
import net.frey.spring6webmvc.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CustomerController.PATH)
@RequiredArgsConstructor
public class CustomerController {
    public static final String PATH = "/api/v1/customer";
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable UUID customerId) {
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.addCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", PATH + savedCustomer.getId()
            .toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        customerService.updateCustomer(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable UUID customerId) {
        customerService.delete(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
