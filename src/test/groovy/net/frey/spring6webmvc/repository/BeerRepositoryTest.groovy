package net.frey.spring6webmvc.repository

import net.frey.spring6webmvc.model.entity.BeerEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class BeerRepositoryTest extends Specification {
    @Autowired
    BeerRepository beerRepository

    def "save a beer"() {
        when:
        def savedBeer = beerRepository.save(BeerEntity.builder()
            .beerName("My Beer")
            .build())

        then:
        savedBeer.beerName == "My Beer"
    }
}
