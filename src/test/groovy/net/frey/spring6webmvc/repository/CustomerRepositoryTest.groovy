package net.frey.spring6webmvc.repository

import net.frey.spring6webmvc.bootstrap.BootstrapData
import net.frey.spring6webmvc.model.entity.CustomerEntity
import net.frey.spring6webmvc.service.BeerCsvServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@DataJpaTest
@Import([BeerCsvServiceImpl, BootstrapData])
class CustomerRepositoryTest extends Specification {
    @Autowired
    CustomerRepository customerRepository

    @Autowired
    BootstrapData bootstrapData

    void setup() {
        bootstrapData.run()
    }

    def "check pre-populated data"() {
        expect:
        customerRepository.count() == 4
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
