package com.krasen.web.services;

import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.models.Role;
import com.krasen.web.models.RoleType;
import com.krasen.web.models.User;
import com.krasen.web.repositories.RoleRepository;
import com.krasen.web.repositories.UserRepository;
import com.krasen.web.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl( UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register( SignUpRequest signUpRequest ) throws Exception {
        if ( userRepository.existsByUsername( signUpRequest.getUsername() ) ) {
            throw new Exception( "Username already exists" );
        }
        if ( userRepository.existsByEmail( signUpRequest.getEmail() ) ) {
            throw new Exception( "Email already in use" );
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add( roleRepository.findByName( RoleType.ROLE_USER ).orElse( null ) );
        User newUser = User.builder()
                .username( signUpRequest.getUsername() )
                .firstName( signUpRequest.getFirstName() )
                .lastName( signUpRequest.getLastName() )
                .password( passwordEncoder.encode( signUpRequest.getPassword() ) )
                .email( signUpRequest.getEmail() )
                .roles( roles )
                .enabled( true )
                .build();

        return userRepository.save( newUser );
    }

    @Override
    public Optional<User> find( Long id ) {
        return userRepository.findById( id );
    }

    @Override
    public Optional<User> findByUsername( String username ) {
        return userRepository.findByUsername( username );
    }

}
