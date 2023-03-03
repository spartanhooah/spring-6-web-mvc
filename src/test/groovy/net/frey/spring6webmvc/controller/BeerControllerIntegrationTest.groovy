package net.frey.spring6webmvc.controller

import jakarta.transaction.Transactional
import net.frey.spring6webmvc.exception.NotFoundException
import net.frey.spring6webmvc.mapper.BeerMapper
import net.frey.spring6webmvc.model.dto.BeerDTO
import net.frey.spring6webmvc.repository.BeerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static java.util.UUID.randomUUID

@SpringBootTest
class BeerControllerIntegrationTest extends Specification {
    @Autowired
    BeerController controller

    @Autowired
    BeerRepository repository

    @Autowired
    BeerMapper mapper

    def "list all beers"() {
        expect:
        controller.listBeers().size() == 3
    }

    @Rollback
    @Transactional
    def "list all beers when there are none"() {
        given:
        repository.deleteAll()

        expect:
        controller.listBeers().size() == 0
    }

    def "get a beer by ID"() {
        given:
        def beer = repository.findAll()[0]

        when:
        def result = controller.getBeerById(beer.id)

        then:
        result
    }

    def "get a beer by a nonexistent ID"() {
        when:
        controller.getBeerById(randomUUID())

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "add a new beer"() {
        given:
        def beerDto = BeerDTO.builder()
            .beerName("New Beer")
            .build()

        when:
        def response = controller.addBeer(beerDto)

        then:
        response.statusCode == HttpStatus.CREATED

        and:
        def savedUuid = response.headers.getLocation()?.path?.split("/")[4]

        then:
        repository.findById(UUID.fromString(savedUuid))
    }

    @Rollback
    @Transactional
    def "update an existing beer"() {
        given:
        def beer = repository.findAll()[0]
        def beerDto = mapper.entityToDto(beer)
        beerDto.id = null
        beerDto.version = 0
        def beerName = "UPDATED"
        beerDto.beerName = beerName

        when:
        def response = controller.updateById(beer.id, beerDto)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
        repository.findById(beer.id).get().beerName == beerName
    }

    def "try to update a non-existent beer"() {
        when:
        controller.updateById(randomUUID(), BeerDTO.builder().build())

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "patch a beer"() {
        given:
        def beerId = repository.findAll()[0].id
        def patchRequest = BeerDTO.builder().id(beerId).beerName("New Name").build()

        when:
        def response = controller.patchBeer(beerId, patchRequest)

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        and:
        repository.findById(beerId).get().beerName == "New Name"
    }

    def "patch a beer that doesn't exist"() {
        given:
        def patchRequest = BeerDTO.builder().build()

        when:
        controller.patchBeer(randomUUID(), patchRequest).statusCode

        then:
        thrown(NotFoundException)
    }

    @Rollback
    @Transactional
    def "delete an existing beer"() {
        given:
        def beer = repository.findAll()[0]

        when:
        def result = controller.deleteById(beer.id)

        then:
        result.statusCode == HttpStatus.NO_CONTENT
        repository.findById(beer.id).isEmpty()
    }

    def "delete a beer that doesn't exist"() {
        when:
        controller.deleteById(randomUUID())

        then:
        thrown(NotFoundException)
    }
}
