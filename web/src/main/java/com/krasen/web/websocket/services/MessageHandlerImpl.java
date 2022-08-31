package com.krasen.web.websocket.services;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import com.krasen.web.models.Room;
import com.krasen.web.websocket.TextSocketMessage;
import com.krasen.web.websocket.services.interfaces.*;

import static com.krasen.web.utils.WebSocketUtils.*;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final RoomHandler roomHandler;

    @Autowired
    public MessageHandlerImpl( RoomHandler roomHandler ) {this.roomHandler = roomHandler;}

    @Override
    public void handleTextMessage( WebSocketSession session, TextMessage message ) throws Exception {
        switch( getTextMessageKey( message ) ) {
            case "signal":
                handleSignalMessage( session, message );
                break;
            case "initSend":
                handleInitSendMessage( session, message );
                break;
            default:
        }
    }

    private void handleSignalMessage( WebSocketSession session, TextMessage message ) throws IOException {
        Room room = roomHandler.getRoomFromSession( session );
        HashMap<String, WebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        String socketId = getTextMessageKeyValueByFieldName( message, "socketId" );
        if( !participants.containsKey( socketId ) ) {
            return;
        }

        LinkedHashMap<String, Object> paramMap = ( LinkedHashMap<String, Object> ) parseTextMessage( message ).getValue();
        paramMap.put( "socketId", session.getId() );

        if( !participants.containsKey( socketId ) ) {
            return;
        }

        synchronized( participants.get( socketId ) ) {
            participants.get( socketId ).sendMessage( buildTextMessage( new TextSocketMessage( "signal", paramMap ) ) );
        }
    }

    private void handleInitSendMessage( WebSocketSession session, TextMessage message ) throws IOException {
        Room room = roomHandler.getRoomFromSession( session );
        HashMap<String, WebSocketSession> participants = roomHandler.getParticipantsFromRoom( room );

        TextSocketMessage textSocketMessage = parseTextMessage( message );

        String initSocketId = ( String ) textSocketMessage.getValue();
        if( !participants.containsKey( initSocketId ) ) {
            return;
        }
        synchronized( participants.get( initSocketId ) ) {
            participants.get( initSocketId ).sendMessage( buildTextMessage( new TextSocketMessage( "initSend", session.getId() ) ) );
        }
    }

}
