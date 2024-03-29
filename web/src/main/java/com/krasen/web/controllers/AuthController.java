package com.krasen.web.controllers;

import com.krasen.web.dtos.LoginRequest;
import com.krasen.web.dtos.LoginResponse;
import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.dtos.SignUpResponse;
import com.krasen.web.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize( "isAnonymous()" )
@RequestMapping( "/api/auth" )
@CrossOrigin( origins = "*" )
public class AuthController {

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
