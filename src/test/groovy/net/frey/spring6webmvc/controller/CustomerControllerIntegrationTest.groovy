package net.frey.spring6webmvc.controller

import jakarta.transaction.Transactional
import net.frey.spring6webmvc.exception.NotFoundException
import net.frey.spring6webmvc.mapper.CustomerMapper
import net.frey.spring6webmvc.model.dto.CustomerDTO
import net.frey.spring6webmvc.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

import static java.util.UUID.randomUUID

@SpringBootTest
class CustomerControllerIntegrationTest extends Specification {
    public static final String CUSTOMER_NAME = "The New Guy"

    @Autowired
    CustomerController controller

    @Autowired
    CustomerRepository repository

    @Autowired
    CustomerMapper mapper

    def "list all customers"() {
        expect:
        controller.listCustomers().size() == 2
    }

    @Rollback
    @Transactional
    def "list all customers when there are none"() {
        given:
        repository.deleteAll()

        expect:
        controller.listCustomers().size() == 0
    }

    def "get a customer by ID"() {
        given:
        def customer = repository.findAll()[0]

        when:
        def result = controller.getCustomerById(customer.id)

        then:
        result
    }

    def "get a customer by a nonexistent ID"() {
        when:
        controller.getCustomerById(randomUUID())

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "add a new customer"() {
        given:
        def customer = CustomerDTO.builder()
            .name(CUSTOMER_NAME)
            .build()

        when:
        def response = controller.addCustomer(customer)

        then:
        response.statusCode == HttpStatus.CREATED

        and:
        def savedUuid = response.headers.getLocation()?.path?.split("/")[4]

        then:
        repository.findById(UUID.fromString(savedUuid))
    }

    @Rollback
    @Transactional
    def "update an existing customer"() {
        given:
        def customer = repository.findAll()[0]
        def customerDto = mapper.entityToDto(customer)
        customerDto.id = null
        customerDto.version = 0
        def customerName = CUSTOMER_NAME
        customerDto.name = customerName

        when:
        def response = controller.updateCustomer(customer.id, customerDto)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
        repository.findById(customer.id).get().name == customerName
    }

    def "try to update a non-existent customer"() {
        when:
        controller.updateCustomer(randomUUID(), CustomerDTO.builder().build())

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "patch a customer"() {
        given:
        def customerId = repository.findAll()[0].id
        def patchRequest = CustomerDTO.builder().id(customerId).name(CUSTOMER_NAME).build()

        when:
        def response = controller.patchCustomer(customerId, patchRequest)

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        and:
        repository.findById(customerId).get().name == CUSTOMER_NAME
    }

    def "patch a customer who doesn't exist"() {
        given:
        def patchRequest = CustomerDTO.builder().build()

        when:
        controller.patchCustomer(randomUUID(), patchRequest)

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "delete an existing customer"() {
        given:
        def customer = repository.findAll()[0]

        when:
        def result = controller.deleteCustomer(customer.id)

        then:
        result.statusCode == HttpStatus.NO_CONTENT
        repository.findById(customer.id).isEmpty()
    }

    def "delete a customer who doesn't exist"() {
        when:
        controller.deleteCustomer(randomUUID())

        then:
        thrown(NotFoundException)
    }
}
