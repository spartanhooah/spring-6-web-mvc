package net.frey.spring6webmvc.service

import org.springframework.util.ResourceUtils
import spock.lang.Specification

class BeerCsvServiceImplTest extends Specification {
    def beerCsvService = new BeerCsvServiceImpl()

    def "convert CSV"() {
        given:
        def file = ResourceUtils.getFile("classpath:csvdata/beers.csv")

        when:
        def records = beerCsvService.convertCsv(file)
        print records.size()

        then:
        records.size() > 0
    }
}
