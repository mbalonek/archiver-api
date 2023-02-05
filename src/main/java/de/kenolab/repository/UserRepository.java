package de.kenolab.repository;

import de.kenolab.entity.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<ArchiveUser, Long> {
       ArchiveUser findByEmail(String email);
}
