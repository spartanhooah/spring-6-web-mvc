package net.frey.spring6webmvc.service;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.hasText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        BeerDTO beer1 =
                BeerDTO.builder()
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

        BeerDTO beer2 =
                BeerDTO.builder()
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

        BeerDTO beer3 =
                BeerDTO.builder()
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

        beerMap =
                new HashMap<>(of(beer1.getId(), beer1, beer2.getId(), beer2, beer3.getId(), beer3));
    }

    @Override
    public List<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory) {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        BeerDTO savedBeer =
                BeerDTO.builder()
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
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existingBeer = beerMap.get(beerId);

        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdatedDate(now());

        return Optional.of(existingBeer);
    }

    @Override
    public boolean delete(UUID beerId) {
        beerMap.remove(beerId);

        return true;
    }

    @Override
    public void patchBeer(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

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
