package net.frey.spring6webmvc.repository

import jakarta.validation.ConstraintViolationException
import net.frey.spring6webmvc.bootstrap.BootstrapData
import net.frey.spring6webmvc.model.BeerStyle
import net.frey.spring6webmvc.model.entity.BeerEntity
import net.frey.spring6webmvc.service.BeerCsvServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@DataJpaTest
@Import([BeerCsvServiceImpl, BootstrapData])
class BeerRepositoryTest extends Specification {
    @Autowired
    BeerRepository beerRepository

    def "check pre-populated data"() {
        expect:
        beerRepository.count() == 2410
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
        beerRepository.save(BeerEntity.builder()
            .beerName("My Beer 123456543212345654321234565432112345654323456543")
            .beerStyle(BeerStyle.ALE)
            .upc("123456")
            .price(10.99)
            .build())

        beerRepository.flush()

        then:
        thrown(ConstraintViolationException)
    }

    def "test get list by name"() {
        when:
        def result = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null)

        then:
        result.size() == 336
    }

    def "test get list by style" () {
        when:
        def result = beerRepository.findAllByBeerStyle(BeerStyle.LAGER, null)

        then:
        result.size() == 39
    }
}
