package com.krasen.web.services;

import com.krasen.web.dtos.LoginRequest;
import com.krasen.web.dtos.LoginResponse;
import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.dtos.SignUpResponse;
import com.krasen.web.enums.RoleType;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.Role;
import com.krasen.web.models.User;
import com.krasen.web.repositories.RoleRepository;
import com.krasen.web.repositories.UserRepository;
import com.krasen.web.services.interfaces.AuthService;
import com.krasen.web.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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
        if ( userRepository.existsByUsername( signUpRequest.getUsername() ) ) {
            throw new GenericException( "Username already exists" );
        }
        if ( userRepository.existsByEmail( signUpRequest.getEmail() ) ) {
            throw new GenericException( "Email already in use" );
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add( roleRepository.findByName( RoleType.ROLE_USER ).orElse( null ) );
        User newUser = User.builder()
                           .id( -1L )
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
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getUsername(),
                                                                                                                     loginRequest.getPassword() ) );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        String token = jwtUtils.generateToken( authentication );
        User userDetails = (User) authentication.getPrincipal();

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
