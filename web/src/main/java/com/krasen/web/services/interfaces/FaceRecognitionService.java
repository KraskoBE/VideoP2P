package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.face.FaceInformation;
import com.krasen.web.dtos.face.VerificationInformation;
import com.krasen.web.models.User;

import java.io.IOException;
import java.util.UUID;

public interface FaceRecognitionService {

    FaceInformation analyze( String imageString );

    VerificationInformation verify( String imageString, UUID roomId, User user ) throws IOException;

    String cropFaceFromImage( FaceInformation faceInformation, String imageString ) throws IOException;

}
