package net.frey.spring6webmvc.repository

import net.frey.spring6webmvc.model.entity.CustomerEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class CustomerRepositoryTest extends Specification {
    @Autowired
    CustomerRepository customerRepository

    def "save a customer"() {
        when:
        def savedCustomer = customerRepository.save(CustomerEntity.builder()
            .name("New Name")
            .build())

        then:
        savedCustomer.name == "New Name"
    }
}
