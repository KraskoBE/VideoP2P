package com.krasen.web.dtos.face;

import java.util.ArrayList;
import java.util.List;

public class FaceAnalyzationRequest {

    public String model_name = "VGG-Face";
    public List<String> img = new ArrayList<>();

    public FaceAnalyzationRequest( String image ) {
        img.add( image );
    }

}
