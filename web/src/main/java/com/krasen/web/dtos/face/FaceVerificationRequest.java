package com.krasen.web.dtos.face;

import java.util.ArrayList;
import java.util.List;

public class FaceVerificationRequest {

    public String model_name = "VGG-Face";
    public List<PhotoPair> img = new ArrayList<>();

    public FaceVerificationRequest( PhotoPair photoPair ) {
        img.add( photoPair );
    }

}
