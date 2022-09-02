package com.krasen.web.websocket.services;

import com.krasen.web.models.Room;
import com.krasen.web.repositories.RoomRepository;
import com.krasen.web.websocket.TextSocketMessage;
import com.krasen.web.websocket.services.interfaces.RoomHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static com.krasen.web.utils.WebSocketUtils.buildTextMessage;
import static java.util.Objects.isNull;

@Service
public class RoomHandlerImpl implements RoomHandler {

    public static final Logger logger = LoggerFactory.getLogger( RoomHandlerImpl.class );
    private static final HashMap<Room, HashMap<String, WebSocketSession>> roomToParticipantsMap = new HashMap<>();

    private final RoomRepository roomRepository;

    @Autowired
    public RoomHandlerImpl( RoomRepository roomRepository ) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void joinRoom( WebSocketSession session ) throws IOException {
        Room room = getRoomFromSession( session );
        if ( isNull( room ) ) {
            return;
        }

        roomToParticipantsMap.putIfAbsent( room, new HashMap<>() );
        HashMap<String, WebSocketSession> participants = roomToParticipantsMap.get( room );

        for ( WebSocketSession webSocketSession : participants.values() ) {
            if ( !webSocketSession.isOpen() ) {
                continue;
            }
            webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "initReceive", session.getId() ) ) );
        }

        participants.put( session.getId(), session );
        logger.info( "{} joined room {}", Objects.requireNonNull( session.getPrincipal() ).getName(), room.getName() );
    }

    @Override
    public void leaveRoom( WebSocketSession session ) throws IOException {
        Room room = getRoomFromSession( session );
        if ( isNull( room ) ) {
            return;
        }

        HashMap<String, WebSocketSession> participants = roomToParticipantsMap.get( room );
        participants.remove( session.getId() );

        if ( participants.isEmpty() ) {
            roomToParticipantsMap.remove( room );
            return;
        }

        for ( WebSocketSession webSocketSession : participants.values() ) {
            if ( session.getId().equals( webSocketSession.getId() ) ) {
                continue;
            }

            if ( webSocketSession.isOpen() ) {
                webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "stopReceive", session.getId() ) ) );
            }
        }
        logger.info( "{} left room {}", Objects.requireNonNull( session.getPrincipal() ).getName(), room.getName() );

    }

    @Override
    public HashMap<String, WebSocketSession> getParticipantsFromRoom( Room room ) {
        return roomToParticipantsMap.get( room );
    }

    @Override
    public Room getRoomFromSession( WebSocketSession session ) throws IOException {
        UUID roomId = (UUID) session.getAttributes().get( "roomId" );

        if ( isNull( roomId ) ) {
            session.close( CloseStatus.BAD_DATA );
            return null;
        }

        Room room = roomRepository.findById( roomId ).orElse( null );
        if ( isNull( room ) ) {
            session.close( CloseStatus.BAD_DATA );
            return null;
        }

        return room;
    }

}
