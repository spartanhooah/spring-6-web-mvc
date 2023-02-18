package net.frey.spring6webmvc.service;

import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.model.Beer;
import net.frey.spring6webmvc.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public Beer getBeerById(UUID id) {
        return Beer.builder()
            .id(id)
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("123456")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(122)
            .createdDate(now())
            .updatedDate(now())
            .build();
    }
}
