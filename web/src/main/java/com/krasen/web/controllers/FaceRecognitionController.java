package com.krasen.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping( "/api/face" )
@CrossOrigin( origins = "*" )
public class FaceRecognitionController {

    @GetMapping
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<String> getAllRooms() {
        String uri = "http://localhost:5000";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject( uri, String.class );
        return ResponseEntity.ok( result );
    }

}
