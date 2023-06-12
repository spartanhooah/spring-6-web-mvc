package net.frey.spring6webmvc.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import net.frey.spring6webmvc.exception.NotFoundException;
import net.frey.spring6webmvc.mapper.BeerMapper;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.dto.BeerDTO;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import net.frey.spring6webmvc.repository.BeerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {
    private final BeerRepository repository;
    private final BeerMapper mapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(
            String name, BeerStyle style, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        Page<BeerEntity> beerPage;

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        if (isNotBlank(name) && style == null) {
            beerPage = listBeersByName(name, pageRequest);
        } else if (StringUtils.isBlank(name) && style != null) {
            beerPage = listBeersByStyle(style, pageRequest);
        } else if (isNotBlank(name) && style != null) {
            beerPage = listBeersByNameAndStyle(name, style, pageRequest);
        } else {
            beerPage = repository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(mapper::entityToDto);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize != null) {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        } else {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<BeerEntity> listBeersByNameAndStyle(String name, BeerStyle style, PageRequest pageRequest) {
        return repository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + name + "%", style, pageRequest);
    }

    private Page<BeerEntity> listBeersByStyle(BeerStyle style, PageRequest pageRequest) {
        return repository.findAllByBeerStyle(style, pageRequest);
    }

    private Page<BeerEntity> listBeersByName(String beerName, PageRequest pageRequest) {
        return repository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
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

                            atomicReference.set(Optional.of(mapper.entityToDto(repository.save(foundBeer))));
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
