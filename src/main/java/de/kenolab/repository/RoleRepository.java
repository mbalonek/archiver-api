package de.kenolab.repository;

import de.kenolab.entity.ArchRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<ArchRole, Long > {
    ArchRole findByName(String name);

}
