package net.frey.spring6webmvc.repository;

import java.util.UUID;
import net.frey.spring6webmvc.model.entity.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {}
