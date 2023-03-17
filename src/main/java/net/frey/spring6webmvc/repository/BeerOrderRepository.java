package net.frey.spring6webmvc.repository;

import java.util.UUID;
import net.frey.spring6webmvc.model.entity.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {}
