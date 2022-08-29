package com.krasen.web.websocket;

import java.io.IOException;
import java.util.*;

import lombok.NonNull;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.krasen.web.utils.WebSocketUtils.*;

@Component
public class SocketHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger( SocketHandler.class );

    Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void handleTextMessage( @NonNull WebSocketSession session, @NonNull TextMessage message ) {
        try {
            switch( getTextMessageKey( message ) ) {
                case "signal":
                    handleSignalMessage( session, message );
                    break;
                case "initSend":
                    handleInitSendMessage( session, message );
                    break;
                default:
            }
        } catch( Exception e ) {
            logger.warn( "[{}] - Couldn't parse message: {}", session.getPrincipal().getName(), message );
        }
    }

    @Override
    public void afterConnectionEstablished( @NonNull WebSocketSession session ) throws Exception {
        for( WebSocketSession webSocketSession : sessions.values() ) {
            if( !webSocketSession.isOpen() ) {
                continue;
            }
            webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "initReceive", session.getId() ) ) );
        }
        sessions.put( session.getId(), session );
    }

    @Override
    public void afterConnectionClosed( @NonNull WebSocketSession session, @NonNull CloseStatus status ) throws IOException {
        sessions.remove( session.getId() );

        for( WebSocketSession webSocketSession : sessions.values() ) {
            if( session.getId().equals( webSocketSession.getId() ) ) {
                continue;
            }
            if( webSocketSession.isOpen() ) {
                webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "stopReceive", session.getId() ) ) );
            }
        }
    }

    private void handleSignalMessage( WebSocketSession session, TextMessage message ) throws IOException {
        String socketId = getTextMessageKeyValueByFieldName( message, "socketId" );
        if( !sessions.containsKey( socketId ) ) {
            return;
        }

        String payload = message.getPayload();
        String newString = payload.substring( 0, payload.length() - 40 ) + String.format( "\"%s\"}}", session.getId() );
        if( !sessions.containsKey( socketId ) ) {
            return;
        }
        sessions.get( socketId ).sendMessage( new TextMessage( newString ) );
    }

    private void handleInitSendMessage( WebSocketSession session, TextMessage message ) throws IOException {
        String initSocketId = ( String ) parseTextMessage( message ).getValue();
        if( !sessions.containsKey( initSocketId ) ) {
            return;
        }
        sessions.get( initSocketId ).sendMessage( buildTextMessage( new TextSocketMessage( "initSend", session.getId() ) ) );
    }

}