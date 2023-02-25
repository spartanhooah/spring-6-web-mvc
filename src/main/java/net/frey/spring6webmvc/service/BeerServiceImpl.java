package net.frey.spring6webmvc.service;

import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.model.Beer;
import net.frey.spring6webmvc.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        Beer beer1 = Beer.builder()
            .id(randomUUID())
            .version(1)
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("123456")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(122)
            .createdDate(now())
            .updatedDate(now())
            .build();

        Beer beer2 = Beer.builder()
            .id(randomUUID())
            .version(1)
            .beerName("Crank")
            .beerStyle(BeerStyle.PALE_ALE)
            .upc("12356222")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(392)
            .createdDate(now())
            .updatedDate(now())
            .build();

        Beer beer3 = Beer.builder()
            .id(randomUUID())
            .version(1)
            .beerName("Sunshine City")
            .beerStyle(BeerStyle.IPA)
            .upc("12346")
            .price(new BigDecimal("13.99"))
            .quantityOnHand(122)
            .createdDate(now())
            .updatedDate(now())
            .build();

        beerMap = new HashMap<>(of(beer1.getId(), beer1, beer2.getId(), beer2, beer3.getId(), beer3));
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {
        return Optional.of(beerMap.get(id));
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
            .id(randomUUID())
            .createdDate(now())
            .updatedDate(now())
            .version(beer.getVersion() == 0 ? 1 : beer.getVersion())
            .beerName(beer.getBeerName())
            .beerStyle(beer.getBeerStyle())
            .quantityOnHand(beer.getQuantityOnHand())
            .upc(beer.getUpc())
            .price(beer.getPrice())
            .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);

        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdatedDate(now());
    }

    @Override
    public void delete(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeer(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);

        if (hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }

        existing.setUpdatedDate(now());
    }
}
