package com.krasen.web.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasen.web.dtos.face.*;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.Room;
import com.krasen.web.models.User;
import com.krasen.web.repositories.RoomRepository;
import com.krasen.web.services.interfaces.FaceRecognitionService;
import com.krasen.web.websocket.services.interfaces.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    private final MessageHandler messageHandler;
    private final RoomRepository roomRepository;

    @Autowired
    public FaceRecognitionServiceImpl( MessageHandler messageHandler, RoomRepository roomRepository ) {
        this.messageHandler = messageHandler;
        this.roomRepository = roomRepository;
    }

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
    public VerificationInformation verify( String imageString, UUID roomId, User user ) throws IOException {
        String uri = "http://localhost:5000/verify";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        Room room = roomRepository.findById( roomId ).orElseThrow( () -> new GenericException( "Room not found" ) );

        VerificationInformation verificationInformation = null;

        try {
            String croppedFace = cropFaceFromImage( analyze( imageString ), imageString );

            FaceVerificationRequest request = new FaceVerificationRequest( new PhotoPair( croppedFace, user.getPictureString() ) );
            String requestResult = restTemplate.postForObject( uri, request, String.class );
            JsonNode root = mapper.readTree( requestResult );

            verificationInformation = mapper.readValue( root.path( "pair_1" ).toString(), VerificationInformation.class );
        } catch ( Exception ignored ) {
        }

        if ( isNull( verificationInformation ) || !verificationInformation.verified ) {
            this.messageHandler.alertForFailedVerification( room, user.getUsername() );
        }

        return verificationInformation;
    }

    @Override
    public String cropFaceFromImage( FaceInformation faceInformation, String imageString ) throws IOException {
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
