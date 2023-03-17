package net.frey.spring6webmvc.repository

import jakarta.transaction.Transactional
import net.frey.spring6webmvc.model.entity.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class CategoryRepositoryTest extends Specification {
    @Autowired
    CategoryRepository categoryRepository

    @Autowired
    BeerRepository beerRepository

    def testBeer

    void setup() {
        testBeer = beerRepository.findAll()[0]
    }

    @Transactional
    def "test add category"() {
        when:
        def savedCategory = categoryRepository.save(Category.builder()
            .description("Ales").build())

        testBeer.addCategory(savedCategory)

        def savedBeer = beerRepository.save(testBeer)

        then:
        println savedBeer
    }
}
