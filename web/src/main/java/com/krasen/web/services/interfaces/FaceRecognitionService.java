package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.face.FaceInformation;
import com.krasen.web.dtos.face.VerificationInformation;
import com.krasen.web.models.User;

public interface FaceRecognitionService {

    FaceInformation analyze( String iamgeString );

    VerificationInformation verify( String imageString, User currentUser );

}
