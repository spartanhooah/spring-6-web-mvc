package net.frey.spring6webmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import net.frey.spring6webmvc.config.SecurityConfig
import net.frey.spring6webmvc.model.dto.BeerDTO
import net.frey.spring6webmvc.service.BeerService
import net.frey.spring6webmvc.service.BeerServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static net.frey.spring6webmvc.controller.BeerController.PATH
import static org.hamcrest.core.Is.is
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Import(SecurityConfig)
@WebMvcTest(BeerController)
class BeerControllerTest extends ControllerTestSetup {
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
        def testBeer = impl.listBeers(null, null, false, 1, 25)[0]
        beerService.getBeerById(testBeer.id) >> Optional.of(testBeer)

        expect:
        mockMvc.perform(get("$PATH/$testBeer.id")
            .with(httpBasic(USERNAME, PASSWORD)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath('$.id', is(testBeer.id.toString())))
            .andExpect(jsonPath('$.beerName', is(testBeer.beerName)))
    }

    def "get all beers"() {
        given:
        beerService.listBeers(*_) >> impl.listBeers(null, null, false, 1, 25)

        expect:
        mockMvc.perform(get(PATH)
            .with(httpBasic(USERNAME, PASSWORD))
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath('$.content.length()', is(3)))
    }

    def "create a beer"() {
        given:
        def beer = impl.listBeers(null, null, false, 1, 25)[0]
        beer.version = 0
        beer.id = null
        beerService.saveNewBeer(_ as BeerDTO) >> impl.listBeers(null, null, false, 1, 25)[1]

        expect:
        mockMvc.perform(post(PATH)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(beer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
    }

    def "update a beer"() {
        given:
        def beer = impl.listBeers(null, null, false, 1, 25)[0]

        1 * beerService.updateBeerById(_ as UUID, _ as BeerDTO) >> Optional.of(beer)

        expect:
        mockMvc.perform(put("$PATH/$beer.id")
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(beer)))
            .andExpect(status().isNoContent())
    }

    def "delete a beer"() {
        given:
        def beer = impl.listBeers(null, null, false, 1, 25)[0]

        1 * beerService.delete({ it == beer.id }) >> true

        expect:
        mockMvc.perform(delete("$PATH/$beer.id")
            .with(httpBasic(USERNAME, PASSWORD)))
            .andExpect(status().isNoContent())
    }

    def "update a beer via PATCH"() {
        given:
        def beer = impl.listBeers(null, null, false, 1, 25)[0]
        def beerMap = ["beerName": "new name"]

        1 * beerService.patchBeer({ it == beer.id }, { it.beerName == "new name" })

        expect:
        mockMvc.perform(patch("$PATH/$beer.id")
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(beerMap)))
            .andExpect(status().isNoContent())
    }

    def "get beer by ID throws not found"() {
        given:
        beerService.getBeerById(_ as UUID) >> Optional.empty()

        expect:
        mockMvc.perform(get("$PATH/${randomUUID()}")
            .with(httpBasic(USERNAME, PASSWORD)))
            .andExpect(status().isNotFound())
    }

    def "return a 400 for adding a beer with no name"() {
        expect:
        mockMvc.perform(post(PATH)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(BeerDTO.builder().build())))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath('$.length()', is(4)))
            .andReturn()
    }

    def "return a 400 for trying to update a beer by sending a blank name"() {
        given:
        def beer = impl.listBeers(null, null, false, 1, 25)[0]
        beer.beerName = ""

        expect:
        mockMvc.perform(put("$PATH/$beer.id")
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(beer)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath('$.length()', is(1)))
            .andReturn()
    }

    def "Return 401 for unauthorized user"() {
        expect:
        mockMvc.perform(get(PATH)
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
    }

    @Ignore("Currently not working as expected")
    def "Return 403 for forbidden user"() {
        expect:
        mockMvc.perform(get(PATH)
            .with(httpBasic(USERNAME, "wrong"))
            .accept(APPLICATION_JSON))
            .andExpect(status().isForbidden())
    }
}
