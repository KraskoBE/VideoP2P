package com.krasen.web.websocket.services;

import com.krasen.web.models.Room;
import com.krasen.web.websocket.TextSocketMessage;
import com.krasen.web.websocket.services.interfaces.MessageHandler;
import com.krasen.web.websocket.services.interfaces.RoomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.krasen.web.utils.WebSocketUtils.*;
import static java.util.Objects.nonNull;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final RoomHandler roomHandler;

    @Autowired
    public MessageHandlerImpl( RoomHandler roomHandler ) {
        this.roomHandler = roomHandler;
    }

    @Override
    public void handleTextMessage( WebSocketSession session, TextMessage message ) throws Exception {
        switch ( getTextMessageKey( message ) ) {
            case "signal":
                handleSignalMessage( (StandardWebSocketSession) session, message );
                break;
            case "initSend":
                handleInitSendMessage( (StandardWebSocketSession) session, message );
                break;
            default:
        }
    }

    private void handleSignalMessage( StandardWebSocketSession session, TextMessage message ) throws IOException {
        Room room = roomHandler.getRoomFromSession( session );
        HashMap<String, StandardWebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        String socketId = getTextMessageKeyValueByFieldName( message, "socketId" );
        if ( !participants.containsKey( socketId ) ) {
            return;
        }

        LinkedHashMap<String, Object> paramMap = (LinkedHashMap<String, Object>) parseTextMessage( message ).getValue();
        paramMap.put( "socketId", session.getId() );

        if ( !participants.containsKey( socketId ) ) {
            return;
        }

        synchronized ( participants.get( socketId ) ) {
            participants.get( socketId ).sendMessage( buildTextMessage( new TextSocketMessage( "signal", paramMap ) ) );
        }
    }

    private void handleInitSendMessage( StandardWebSocketSession session, TextMessage message ) throws IOException {
        Room room = roomHandler.getRoomFromSession( session );
        HashMap<String, StandardWebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        TextSocketMessage textSocketMessage = parseTextMessage( message );

        String initSocketId = (String) textSocketMessage.getValue();
        if ( !participants.containsKey( initSocketId ) ) {
            return;
        }
        synchronized ( participants.get( initSocketId ) ) {
            participants.get( initSocketId ).sendMessage( buildTextMessage( new TextSocketMessage( "initSend", session.getId() ) ) );
        }
    }

    @Override
    public void alertForFailedVerification( Room room, String alert ) throws IOException {
        HashMap<String, StandardWebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        StandardWebSocketSession roomCreatorSession = null;

        for ( Map.Entry<String, StandardWebSocketSession> entry : participants.entrySet() ) {
            if ( entry.getValue().getPrincipal().getName().equals( room.getCreatedBy().getUsername() ) ) {
                roomCreatorSession = entry.getValue();
            }
        }

        if ( nonNull( roomCreatorSession ) ) {
            synchronized ( roomCreatorSession ) {
                roomCreatorSession.sendMessage( buildTextMessage( new TextSocketMessage( "alert", alert ) ) );
            }
        }
    }

}
