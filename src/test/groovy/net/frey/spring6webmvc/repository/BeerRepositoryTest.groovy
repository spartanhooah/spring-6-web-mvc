package net.frey.spring6webmvc.repository

import jakarta.validation.ConstraintViolationException
import net.frey.spring6webmvc.model.BeerStyle
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
            .beerStyle(BeerStyle.ALE)
            .upc("123456")
            .price(10.99)
            .build())

        beerRepository.flush()

        then:
        savedBeer.beerName == "My Beer"
    }

    def "save a beer with a long name"() {
        when:
        def savedBeer = beerRepository.save(BeerEntity.builder()
            .beerName("My Beer 123456543212345654321234565432112345654323456543")
            .beerStyle(BeerStyle.ALE)
            .upc("123456")
            .price(10.99)
            .build())

        beerRepository.flush()

        then:
        thrown(ConstraintViolationException)
    }
}
