package com.krasen.web.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krasen.web.models.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName( RoleType name );

}
