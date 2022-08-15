package com.krasen.web.configuration.websocket;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.krasen.web.utils.WebSocketUtils.*;

@Component
public class SocketHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger( SocketHandler.class );

    Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void handleTextMessage( @NonNull WebSocketSession session, @NonNull TextMessage message ) throws IOException {
        switch ( getTextMessageKey( message ) ) {
            case "signal":
                handleSignalMessage( session, message );
                break;
            case "initSend":
                handleInitSendMessage( session, message );
                break;
            default:
        }
    }

    @Override
    public void afterConnectionEstablished( @NonNull WebSocketSession session ) throws Exception {
        for ( WebSocketSession webSocketSession : sessions.values() ) {
            if ( !webSocketSession.isOpen() ) {
                logger.warn( "Trying to write to a closed session!" );
                continue;
            }
            webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "initReceive", session.getId() ) ) );
        }
        sessions.put( session.getId(), session );
    }

    @Override
    public void afterConnectionClosed( @NonNull WebSocketSession session, @NonNull CloseStatus status ) throws IOException {
        sessions.remove( session.getId() );

        for ( WebSocketSession webSocketSession : sessions.values() ) {
            if ( session.getId().equals( webSocketSession.getId() ) ) {
                continue;
            }
            if ( webSocketSession.isOpen() ) {
                webSocketSession.sendMessage( buildTextMessage( new TextSocketMessage( "stopReceive", session.getId() ) ) );
            }
        }
    }

    private void handleSignalMessage( WebSocketSession session, TextMessage message ) throws IOException {
        String socketId = getTextMessageKeyValueByFieldName( message, "socketId" );
        if ( !sessions.containsKey( socketId ) ) {
            logger.warn( "Trying to write to a closed session!" );
            return;
        }

        String payload = message.getPayload();
        String newString = payload.substring( 0, payload.length() - 40 ) + String.format( "\"%s\"}}", session.getId() );
        if ( !sessions.containsKey( socketId ) ) {
            logger.warn( "Trying to write to a closed session!" );
            return;
        }
        sessions.get( socketId ).sendMessage( new TextMessage( newString ) );
    }

    private void handleInitSendMessage( WebSocketSession session, TextMessage message ) throws IOException {
        String initSocketId = (String) parseTextMessage( message ).getValue();
        if ( !sessions.containsKey( initSocketId ) ) {
            logger.warn( "Trying to write to a closed session!" );
            return;
        }
        sessions.get( initSocketId ).sendMessage( buildTextMessage( new TextSocketMessage( "initSend", session.getId() ) ) );
    }

}