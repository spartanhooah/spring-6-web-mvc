package net.frey.spring6webmvc.repository;

import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CommandLineRunner.class}))
public class BeerRepoJavaTest {
    @Autowired
    BeerRepository repository;

    @Test
    void testSaveBeer() {
        BeerEntity savedBeer = repository.save(BeerEntity.builder()
            .beerName("My Beer")
            .build());

        repository.flush();

        assertThat(savedBeer, is(notNullValue()));
    }
}
