package de.kenolab.security.repository;

import de.kenolab.security.entity.ArchRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<ArchRole, Long > {
    ArchRole findByName(String name);

}
