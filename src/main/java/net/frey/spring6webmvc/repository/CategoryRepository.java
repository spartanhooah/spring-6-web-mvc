package net.frey.spring6webmvc.repository;

import java.util.UUID;
import net.frey.spring6webmvc.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {}
