package com.krasen.web.services;

import com.krasen.web.dtos.face.FaceInformation;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.Room;
import com.krasen.web.models.User;
import com.krasen.web.repositories.UserRepository;
import com.krasen.web.services.interfaces.FaceRecognitionService;
import com.krasen.web.services.interfaces.RoomService;
import com.krasen.web.services.interfaces.UserService;
import com.krasen.web.websocket.services.interfaces.RoomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.util.HashMap;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FaceRecognitionService faceRecognitionService;
    private final RoomService roomService;
    private final RoomHandler roomHandler;

    @Autowired
    public UserServiceImpl( UserRepository userRepository,
                            FaceRecognitionService faceRecognitionService,
                            RoomService roomService,
                            RoomHandler roomHandler ) {
        this.userRepository = userRepository;
        this.faceRecognitionService = faceRecognitionService;
        this.roomService = roomService;
        this.roomHandler = roomHandler;
    }

    @Override
    public User updatePicture( String pictureString, User currentUser ) {
        String croppedPicture;

        try {
            FaceInformation analyze = faceRecognitionService.analyze( pictureString );
            croppedPicture = faceRecognitionService.cropFaceFromImage( analyze, pictureString );
        } catch ( Exception e ) {
            throw new GenericException( "Face not found on image" );
        }

        currentUser.setPictureString( croppedPicture );
        return userRepository.save( currentUser );
    }

    @Override
    public User getUserFromSessionIdAndRoomId( String sessionId, UUID roomId ) {
        Room room = roomService.getRoomById( roomId );
        HashMap<String, StandardWebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) participants.get( sessionId ).getPrincipal();
        if ( isNull( token ) || isNull( token.getPrincipal() ) ) {
            throw new GenericException( "User not found" );
        }

        return (User) token.getPrincipal();
    }

}
