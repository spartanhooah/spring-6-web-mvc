package net.frey.spring6webmvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BeerControllerTest extends Specification {
    @Autowired
    BeerController testSubject

    def "get beer by ID"() {
        when:
        def result = testSubject.listBeers()

        then:
        result
        println result
    }
}
