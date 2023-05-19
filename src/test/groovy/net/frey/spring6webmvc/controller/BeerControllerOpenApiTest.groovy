package net.frey.spring6webmvc.controller

import com.tomtom.http.HttpClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BeerControllerOpenApiTest extends Specification {
//    @Shared
    @LocalServerPort
    Integer localPort

//    @Shared
    HttpClient beerClient

//    void setupSpec() {
//        beerClient = new HttpClient(baseUrl: "http://localhost:$localPort")
//    }
    void setup() {
        beerClient = new HttpClient(baseUrl: "http://localhost:$localPort")
    }

    def "test list beers"() {
        when:
        def response = beerClient.get(
            path: "/api/v1/beer",
            headers: ["Content-Type": "application/json"]
        )

        then:
        response.statusCode == 200
    }
}
