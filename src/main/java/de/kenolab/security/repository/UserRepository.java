package de.kenolab.security.repository;

import de.kenolab.security.entity.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<ArchiveUser, Long> {
       ArchiveUser findByEmail(String email);
}
