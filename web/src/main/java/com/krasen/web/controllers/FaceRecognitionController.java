package com.krasen.web.controllers;

import com.krasen.web.dtos.face.FaceInformation;
import com.krasen.web.dtos.face.VerificationInformation;
import com.krasen.web.models.User;
import com.krasen.web.services.interfaces.FaceRecognitionService;
import com.krasen.web.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping( "/api/face" )
@CrossOrigin( origins = "*" )
public class FaceRecognitionController {


    private final FaceRecognitionService faceRecognitionService;

    @Autowired
    public FaceRecognitionController( FaceRecognitionService faceRecognitionService ) {
        this.faceRecognitionService = faceRecognitionService;
    }

    @PostMapping( "/analyze" )
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<FaceInformation> analyze( @RequestBody final String imageString ) {
        return ResponseEntity.ok( faceRecognitionService.analyze( imageString ) );
    }

    @PostMapping( "/verify/{roomId}" )
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<VerificationInformation> verify( @RequestBody final String imageString,
                                                           @PathVariable UUID roomId,
                                                           @CurrentUser final User currentUser ) throws IOException {
        return ResponseEntity.ok( faceRecognitionService.verify( imageString, roomId, currentUser ) );
    }

}
