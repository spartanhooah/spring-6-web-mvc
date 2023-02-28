package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.dto.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    void updateBeerById(UUID beerId, BeerDTO beer);

    void delete(UUID beerId);

    void patchBeer(UUID beerId, BeerDTO beer);
}
