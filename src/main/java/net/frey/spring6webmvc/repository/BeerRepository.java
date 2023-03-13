package net.frey.spring6webmvc.repository;

import java.util.List;
import java.util.UUID;

import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {
    List<BeerEntity> findAllByBeerNameIsLikeIgnoreCase(String name);
    List<BeerEntity> findAllByBeerStyle(BeerStyle style);
    List<BeerEntity> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String name, BeerStyle style);
}
