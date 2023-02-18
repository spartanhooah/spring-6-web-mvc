package net.frey.spring6webmvc.service;

import net.frey.spring6webmvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
