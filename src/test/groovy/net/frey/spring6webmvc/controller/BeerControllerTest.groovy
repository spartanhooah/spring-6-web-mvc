package net.frey.spring6webmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import net.frey.spring6webmvc.exception.NotFoundException
import net.frey.spring6webmvc.model.Beer
import net.frey.spring6webmvc.service.BeerService
import net.frey.spring6webmvc.service.BeerServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static net.frey.spring6webmvc.controller.BeerController.PATH
import static org.hamcrest.core.Is.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BeerController)
class BeerControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper mapper

    @SpringBean
    BeerService beerService = Mock()

    BeerService impl

    void setup() {
        impl = new BeerServiceImpl()
    }

    def "get beer by ID"() {
        given:
        def testBeer = impl.listBeers()[0]
        beerService.getBeerById(testBeer.id) >> Optional.of(testBeer)

        expect:
        mockMvc.perform(get("$PATH/$testBeer.id")
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
        mockMvc.perform(get(PATH)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.length()', is(3)))
    }

    def "create a beer"() {
        given:
        def beer = impl.listBeers()[0]
        beer.version = 0
        beer.id = null
        beerService.saveNewBeer(_ as Beer) >> impl.listBeers()[1]

        expect:
        mockMvc.perform(post(PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(beer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
    }

    def "update a beer"() {
        given:
        def beer = impl.listBeers()[0]

        1 * beerService.updateBeerById(_ as UUID, _ as Beer)

        expect:
        mockMvc.perform(put("$PATH/$beer.id")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(beer)))
            .andExpect(status().isNoContent())
    }

    def "delete a beer"() {
        given:
        def beer = impl.listBeers()[0]

        1 * beerService.delete({ it == beer.id })

        expect:
        mockMvc.perform(delete("$PATH/$beer.id"))
            .andExpect(status().isNoContent())
    }

    def "update a beer via PATCH"() {
        given:
        def beer = impl.listBeers()[0]
        def beerMap = ["beerName": "new name"]

        1 * beerService.patchBeer({ it == beer.id }, { it.beerName == "new name" })

        expect:
        mockMvc.perform(patch("$PATH/$beer.id")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(beerMap)))
            .andExpect(status().isNoContent())
    }

    def "get beer by ID throws not found"() {
        given:
        beerService.getBeerById(_ as UUID) >> Optional.empty()

        expect:
        mockMvc.perform(get("$PATH/${randomUUID()}"))
            .andExpect(status().isNotFound())
    }
}
