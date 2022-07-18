package com.krasen.web.repositories;

import com.krasen.web.models.Role;
import com.krasen.web.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName( RoleType name );
}
