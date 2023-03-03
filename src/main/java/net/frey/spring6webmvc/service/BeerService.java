package net.frey.spring6webmvc.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.frey.spring6webmvc.model.dto.BeerDTO;

public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    boolean delete(UUID beerId);

    void patchBeer(UUID beerId, BeerDTO beer);
}
