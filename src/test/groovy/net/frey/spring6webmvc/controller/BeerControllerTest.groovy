package net.frey.spring6webmvc.controller

import net.frey.spring6webmvc.service.BeerService
import net.frey.spring6webmvc.service.BeerServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.hamcrest.core.Is.is

@WebMvcTest(BeerController)
class BeerControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpringBean
    BeerService beerService = Mock()

    def impl = new BeerServiceImpl()

    def "get beer by ID"() {
        given:
        def testBeer = impl.listBeers()[0]
        beerService.getBeerById(testBeer.id) >> testBeer

        expect:
        mockMvc.perform(get("/api/v1/beer/$testBeer.id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.id', is(testBeer.id.toString())))
            .andExpect(jsonPath('$.beerName', is(testBeer.beerName)))
    }

    def "get all beers"() {
        given:
        beerService.listBeers() >> impl.listBeers()

        expect:
        mockMvc.perform(get("/api/v1/beer")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.length()', is(3)))
    }
}
