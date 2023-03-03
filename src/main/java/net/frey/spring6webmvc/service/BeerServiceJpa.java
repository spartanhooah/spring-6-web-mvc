package net.frey.spring6webmvc.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.mapper.BeerMapper;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import net.frey.spring6webmvc.repository.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {
    private final BeerRepository repository;
    private final BeerMapper mapper;

    @Override
    public List<BeerDTO> listBeers() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(mapper.entityToDto(repository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return mapper.entityToDto(repository.save(mapper.dtoToEntity(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        repository
                .findById(beerId)
                .ifPresentOrElse(
                        foundBeer -> {
                            foundBeer.setBeerName(beer.getBeerName());
                            foundBeer.setBeerStyle(beer.getBeerStyle());
                            foundBeer.setUpc(beer.getUpc());
                            foundBeer.setPrice(beer.getPrice());

                            atomicReference.set(
                                    Optional.of(mapper.entityToDto(repository.save(foundBeer))));
                        },
                        () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public boolean delete(UUID beerId) {
        if (repository.existsById(beerId)) {
            repository.deleteById(beerId);

            return true;
        }

        return false;
    }

    @Override
    public void patchBeer(UUID beerId, BeerDTO beer) {
        repository
                .findById(beerId)
                .ifPresentOrElse(
                        foundBeer -> {
                            if (beer.getBeerName() != null) {
                                foundBeer.setBeerName(beer.getBeerName());
                            }

                            if (beer.getBeerStyle() != null) {
                                foundBeer.setBeerStyle(beer.getBeerStyle());
                            }

                            if (beer.getUpc() != null) {
                                foundBeer.setUpc(beer.getUpc());
                            }

                            if (beer.getPrice() != null) {
                                foundBeer.setPrice(beer.getPrice());
                            }

                            repository.save(foundBeer);
                        },
                        () -> {
                            throw new NotFoundException();
                        });
    }
}
