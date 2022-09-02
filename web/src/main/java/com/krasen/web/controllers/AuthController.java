package com.krasen.web.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.krasen.web.dtos.*;
import com.krasen.web.services.interfaces.AuthService;

@RestController
@PreAuthorize( "isAnonymous()" )
@RequestMapping( "/api/auth" )
@CrossOrigin( origins = "*" )
public class AuthController {

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController( AuthService authService ) {
        this.authService = authService;
    }

    @PostMapping( "/signup" )
    public ResponseEntity<SignUpResponse> signupUser( @Valid @RequestBody final SignUpRequest signUpRequest ) throws Exception {
        return ResponseEntity.ok( authService.register( signUpRequest ) );
    }

    @PostMapping( "/login" )
    public ResponseEntity<LoginResponse> loginUser( @Valid @RequestBody final LoginRequest loginRequest ) {
        return ResponseEntity.ok( authService.login( loginRequest ) );
    }

}
