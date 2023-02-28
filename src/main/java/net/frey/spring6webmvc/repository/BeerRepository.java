package net.frey.spring6webmvc.repository;

import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {
}
