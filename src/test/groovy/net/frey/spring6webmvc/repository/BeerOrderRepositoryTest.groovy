package net.frey.spring6webmvc.repository

import jakarta.transaction.Transactional
import net.frey.spring6webmvc.model.entity.BeerOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BeerOrderRepositoryTest extends Specification {
    @Autowired
    BeerOrderRepository beerOrderRepository

    @Autowired
    CustomerRepository customerRepository

    @Autowired
    BeerRepository beerRepository

    def testCustomer
    def testBeer

    void setup() {
        testBeer = beerRepository.findAll()[0]
        testCustomer = customerRepository.findAll()[0]
    }

    @Transactional
    def "test beer orders"() {
        given:
        def order = BeerOrder.builder()
            .customerRef("Test order")
            .customer(testCustomer)
            .build()

        when:
        def savedOrder = beerOrderRepository.save(order)

        then:
        println savedOrder.customerRef
    }
}
