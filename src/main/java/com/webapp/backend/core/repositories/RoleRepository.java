package com.webapp.backend.core.repositories;

import com.webapp.backend.core.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT e FROM Role e WHERE e.name = ?1")
    Role findByName(String name);
}
