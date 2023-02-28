package net.frey.spring6webmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import net.frey.spring6webmvc.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(BeerController.PATH)
@RequiredArgsConstructor
public class BeerController {
    public static final String PATH = "/api/v1/beer";
    private final BeerService beerService;

    // @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    @GetMapping
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping("/{beerId}")
    public BeerDTO getBeerById(@PathVariable UUID beerId) {
        log.info("Getting a beer with id " + beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    // @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<BeerDTO> addBeer(@RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", PATH + savedBeer.getId()
            .toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> updateById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<BeerDTO> deleteById(@PathVariable UUID beerId) {
        beerService.delete(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{beerId}")
    public ResponseEntity<BeerDTO> patchBeer(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        beerService.patchBeer(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
