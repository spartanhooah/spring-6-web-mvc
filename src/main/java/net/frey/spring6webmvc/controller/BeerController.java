package net.frey.spring6webmvc.controller;

import jakarta.validation.Valid;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import net.frey.spring6webmvc.service.BeerService;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(BeerController.PATH)
@RequiredArgsConstructor
public class BeerController {
    public static final String PATH = "/api/v1/beer";
    private final BeerService beerService;

    // @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    @GetMapping
    public Page<BeerDTO> listBeers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BeerStyle style,
            @RequestParam(required = false) Boolean showInventory,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerService.listBeers(name, style, showInventory, pageNumber, pageSize);
    }

    @GetMapping("/{beerId}")
    public BeerDTO getBeerById(@PathVariable UUID beerId) {
        log.info("Getting a beer with id " + beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    // @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<BeerDTO> addBeer(@Valid @RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> updateById(
            @PathVariable UUID beerId, @Valid @RequestBody BeerDTO beer) {
        if (beerService.updateBeerById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<BeerDTO> deleteById(@PathVariable UUID beerId) {
        if (beerService.delete(beerId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        throw new NotFoundException();
    }

    @PatchMapping("/{beerId}")
    public ResponseEntity<BeerDTO> patchBeer(
            @PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        beerService.patchBeer(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
