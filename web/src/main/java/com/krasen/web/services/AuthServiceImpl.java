package com.krasen.web.services;

import java.util.HashSet;

import com.krasen.web.controllers.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.krasen.web.dtos.*;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.*;
import com.krasen.web.repositories.*;
import com.krasen.web.services.interfaces.AuthService;
import com.krasen.web.utils.JwtUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl( UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager,
                            JwtUtils jwtUtils ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SignUpResponse register( SignUpRequest signUpRequest ) {
        if( userRepository.existsByUsername( signUpRequest.getUsername() ) ) {
            throw new GenericException( "Username already exists" );
        }
        if( userRepository.existsByEmail( signUpRequest.getEmail() ) ) {
            throw new GenericException( "Email already in use" );
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
                           .accountNonExpired( true )
                           .accountNonLocked( true )
                           .credentialsNonExpired( true )
                           .build();

        User savedUser = userRepository.save( newUser );
        return SignUpResponse.builder()
                             .id( savedUser.getId() )
                             .username( savedUser.getUsername() )
                             .email( savedUser.getEmail() )
                             .firstName( savedUser.getFirstName() )
                             .lastName( newUser.getLastName() )
                             .build();
    }

    @Override
    public LoginResponse login( LoginRequest loginRequest ) {
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword() ) );
        AuthController.logger.info("1");

        SecurityContextHolder.getContext().setAuthentication( authentication );
        AuthController.logger.info("2");

        String token = jwtUtils.generateToken( authentication );

        AuthController.logger.info("3");

        User userDetails = ( User ) authentication.getPrincipal();
        AuthController.logger.info("4");

        return LoginResponse.builder()
                            .id( userDetails.getId() )
                            .token( token )
                            .username( userDetails.getUsername() )
                            .email( userDetails.getEmail() )
                            .firstName( userDetails.getFirstName() )
                            .lastName( userDetails.getLastName() )
                            .build();
    }

}
