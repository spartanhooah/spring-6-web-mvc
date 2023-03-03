package net.frey.spring6webmvc.repository

import net.frey.spring6webmvc.model.entity.BeerEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import spock.lang.Specification

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [CommandLineRunner]))
class BeerRepositoryTest extends Specification {
    @Autowired
    BeerRepository beerRepository

    def "check pre-populated data"() {
        expect:
        beerRepository.count() == 3
    }

    def "save a beer"() {
        when:
        def savedBeer = beerRepository.save(BeerEntity.builder()
            .beerName("My Beer")
            .build())

        then:
        savedBeer.beerName == "My Beer"
    }
}
