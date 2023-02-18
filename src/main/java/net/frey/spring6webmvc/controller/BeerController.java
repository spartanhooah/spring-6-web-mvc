package net.frey.spring6webmvc.controller;

import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.model.Beer;
import net.frey.spring6webmvc.service.BeerService;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        return beerService.getBeerById(id);
    }
}
