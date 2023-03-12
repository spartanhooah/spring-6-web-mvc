package net.frey.spring6webmvc.bootstrap;

import static java.time.LocalDateTime.now;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import net.frey.spring6webmvc.model.entity.CustomerEntity;
import net.frey.spring6webmvc.repository.BeerRepository;
import net.frey.spring6webmvc.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        BeerEntity beer1 =
                BeerEntity.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .upc("123456")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(now())
                        .updatedDate(now())
                        .build();

        BeerEntity beer2 =
                BeerEntity.builder()
                        .beerName("Crank")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .upc("12356222")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(392)
                        .createdDate(now())
                        .updatedDate(now())
                        .build();

        BeerEntity beer3 =
                BeerEntity.builder()
                        .beerName("Sunshine City")
                        .beerStyle(BeerStyle.IPA)
                        .upc("12346")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(122)
                        .createdDate(now())
                        .updatedDate(now())
                        .build();

        beerRepository.saveAll(List.of(beer1, beer2, beer3));

        CustomerEntity customer1 =
                CustomerEntity.builder()
                        .name("Billie Jean")
                        .createdDate(now())
                        .lastModified(now())
                        .build();

        CustomerEntity customer2 =
                CustomerEntity.builder()
                        .name("Jim Bob")
                        .version(1)
                        .createdDate(now())
                        .lastModified(now())
                        .build();

        customerRepository.saveAll(List.of(customer1, customer2));
    }
}
