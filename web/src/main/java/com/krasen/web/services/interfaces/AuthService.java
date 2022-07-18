package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.models.User;

import java.util.Optional;

public interface AuthService {

    User register( SignUpRequest user ) throws Exception;

    Optional<User> find( Long id );

    Optional<User> findByUsername( String username );
}