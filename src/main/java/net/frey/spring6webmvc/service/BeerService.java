package net.frey.spring6webmvc.service;

import java.util.Optional;
import java.util.UUID;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import org.springframework.data.domain.Page;

public interface BeerService {
    Page<BeerDTO> listBeers(
            String name,
            BeerStyle style,
            Boolean showInventory,
            Integer pageNumber,
            Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    boolean delete(UUID beerId);

    void patchBeer(UUID beerId, BeerDTO beer);
}
