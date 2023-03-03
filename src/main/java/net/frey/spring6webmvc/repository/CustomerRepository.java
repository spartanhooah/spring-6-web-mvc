package net.frey.spring6webmvc.repository;

import java.util.UUID;
import net.frey.spring6webmvc.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {}
