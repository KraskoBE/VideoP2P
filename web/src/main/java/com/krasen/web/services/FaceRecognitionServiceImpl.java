package com.krasen.web.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasen.web.dtos.face.*;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.User;
import com.krasen.web.services.interfaces.FaceRecognitionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    @Override
    public FaceInformation analyze( String imageString ) {

        String uri = "http://localhost:5000/analyze";
        RestTemplate restTemplate = new RestTemplate();
        FaceAnalyzationRequest faceAnalyzationRequest = new FaceAnalyzationRequest( imageString );
        String requestResult = restTemplate.postForObject( uri, faceAnalyzationRequest, String.class );
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree( requestResult );
            return new ObjectMapper().readValue( root.path( "instance_1" ).toString(), FaceInformation.class );
        } catch ( JsonProcessingException e ) {
            throw new GenericException( "Couldn't process Python API request" );
        }
    }

    @Override
    public VerificationInformation verify( String imageString, User currentUser ) {
        String uri = "http://localhost:5000/verify";
        RestTemplate restTemplate = new RestTemplate();

        FaceInformation faceInformation1 = analyze( imageString );
        FaceInformation faceInformation2 = analyze( currentUser.getPictureString() );


        String face1;
        String face2;
        try {
            face1 = cropFaceFromImage( faceInformation1, imageString );
            face2 = cropFaceFromImage( faceInformation2, currentUser.getPictureString() );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }

        FaceVerificationRequest request = new FaceVerificationRequest( new PhotoPair( face1, face2 ) );
        String requestResult = restTemplate.postForObject( uri, request, String.class );

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree( requestResult );
            return new ObjectMapper().readValue( root.path( "pair_1" ).toString(), VerificationInformation.class );
        } catch ( JsonProcessingException e ) {
            throw new GenericException( "Couldn't process Python API request" );
        }
    }

    private String cropFaceFromImage( FaceInformation faceInformation, String imageString ) throws IOException {
        String base64Image = imageString.split( "," )[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary( base64Image );

        BufferedImage img = ImageIO.read( new ByteArrayInputStream( imageBytes ) );

        BufferedImage croppedImage =
                img.getSubimage( faceInformation.region.x, faceInformation.region.y, faceInformation.region.w, faceInformation.region.h );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write( croppedImage, "PNG", out );
        byte[] bytes = out.toByteArray();

        String base64bytes = Base64.getEncoder().encodeToString( bytes );

        return "data:image/png;base64," + base64bytes;
    }

}
