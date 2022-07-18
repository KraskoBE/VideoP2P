package com.krasen.web.controllers;

import com.krasen.web.configuration.security.JwtUtils;
import com.krasen.web.dtos.JwtResponse;
import com.krasen.web.dtos.LoginRequest;
import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.models.User;
import com.krasen.web.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize( "isAnonymous()" )
@RequestMapping( "/api/auth" )
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController( AuthService authService,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping( "/signup" )
    public ResponseEntity<?> signupUser( @Valid @RequestBody SignUpRequest signUpRequest ) throws Exception {
        return ResponseEntity.ok( authService.register( signUpRequest ) );
    }

    @PostMapping( "/login" )
    public ResponseEntity<?> loginUser( @Valid @RequestBody LoginRequest loginRequest ) {
        Authentication authentication =
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getUsername(),
                        loginRequest.getPassword() ) );
        SecurityContextHolder.getContext().setAuthentication( authentication );
        String jwt = jwtUtils.generateJwtToken( authentication );

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map( GrantedAuthority::getAuthority ).collect( Collectors.toList() );
        return ResponseEntity.ok( new JwtResponse( userDetails.getId(), jwt, userDetails.getUsername(), userDetails.getEmail(), roles ) );
    }

    @GetMapping( "/test" )
    @PreAuthorize( "hasRole('USER')" )
    public String test() throws Exception {
        return "success";
    }
}
