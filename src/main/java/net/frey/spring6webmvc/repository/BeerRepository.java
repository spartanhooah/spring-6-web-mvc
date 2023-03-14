package net.frey.spring6webmvc.repository;

import java.util.UUID;
import net.frey.spring6webmvc.model.BeerStyle;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {
    Page<BeerEntity> findAllByBeerNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<BeerEntity> findAllByBeerStyle(BeerStyle style, Pageable pageable);

    Page<BeerEntity> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String name, BeerStyle style, Pageable pageable);
}
