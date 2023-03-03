package net.frey.spring6webmvc.repository

import net.frey.spring6webmvc.model.entity.CustomerEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import spock.lang.Specification

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [CommandLineRunner]))
class CustomerRepositoryTest extends Specification {
    @Autowired
    CustomerRepository customerRepository

    def "check pre-populated data"() {
        expect:
        customerRepository.count() == 2
    }

    def "save a customer"() {
        when:
        def savedCustomer = customerRepository.save(CustomerEntity.builder()
            .name("New Name")
            .build())

        then:
        savedCustomer.name == "New Name"
    }
}
