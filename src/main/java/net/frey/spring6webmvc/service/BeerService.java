package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void delete(UUID beerId);

    void patchBeer(UUID beerId, Beer beer);
}
