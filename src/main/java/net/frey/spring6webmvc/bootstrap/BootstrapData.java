package net.frey.spring6webmvc.bootstrap;

import static java.time.LocalDateTime.now;

import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import net.frey.spring6webmvc.model.entity.CustomerEntity;
import net.frey.spring6webmvc.repository.BeerRepository;
import net.frey.spring6webmvc.repository.CustomerRepository;
import net.frey.spring6webmvc.service.BeerCsvServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvServiceImpl beerCsvService;

    @Override
    @Transactional
    public void run(String... args) throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            loadBeersFromCsv();
        }

        if (customerRepository.count() < 5) {
            loadCustomers();
        }
    }

    private void loadCustomers() {
        CustomerEntity customer1 = CustomerEntity.builder()
                .name("Billie Jean")
                .createdDate(now())
                .lastModified(now())
                .build();

        CustomerEntity customer2 = CustomerEntity.builder()
                .name("Jim Bob")
                .version(1)
                .createdDate(now())
                .lastModified(now())
                .build();

        customerRepository.saveAll(List.of(customer1, customer2));
    }

    private void loadBeersFromCsv() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        beerCsvService.convertCsv(file).forEach(beerCSVRecord -> {
            BeerStyle beerStyle =
                    switch (beerCSVRecord.getStyle()) {
                        case "American Pale Lager" -> BeerStyle.LAGER;
                        case "American Pale Ale (APA)",
                                "American Black Ale",
                                "Belgian Dark Ale",
                                "American Blonde Ale" -> BeerStyle.ALE;
                        case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                        case "American Porter" -> BeerStyle.PORTER;
                        case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                        case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                        case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                        case "English Pale Ale" -> BeerStyle.PALE_ALE;
                        default -> BeerStyle.PILSNER;
                    };

            beerRepository.save(BeerEntity.builder()
                    .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                    .beerStyle(beerStyle)
                    .price(BigDecimal.TEN)
                    .upc(String.valueOf(beerCSVRecord.getRow()))
                    .quantityOnHand(beerCSVRecord.getCount_x())
                    .build());
        });
    }
}
