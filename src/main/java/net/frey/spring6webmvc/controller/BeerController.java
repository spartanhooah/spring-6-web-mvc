package net.frey.spring6webmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.model.Beer;
import net.frey.spring6webmvc.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/beer")
@RequiredArgsConstructor
public class BeerController {
    private final BeerService beerService;

//    @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<Beer> addBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

//    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    @GetMapping("/{beerId}")
    public Beer getBeerById(@PathVariable UUID beerId) {
        log.info("Getting a beer with id " + beerId);
        return beerService.getBeerById(beerId);
    }
}
