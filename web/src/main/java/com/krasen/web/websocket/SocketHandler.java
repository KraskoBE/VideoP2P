package com.krasen.web.websocket;

import java.io.IOException;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.krasen.web.websocket.services.interfaces.*;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private final RoomHandler roomHandler;
    private final MessageHandler messageHandler;

    @Autowired
    public SocketHandler( RoomHandler roomHandler, MessageHandler messageHandler ) {
        this.roomHandler = roomHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void handleTextMessage( @NonNull WebSocketSession session, @NonNull TextMessage message ) throws Exception{
        messageHandler.handleTextMessage( session, message );
    }

    @Override
    public void afterConnectionEstablished( @NonNull WebSocketSession session ) throws Exception {
        roomHandler.joinRoom( session );
    }

    @Override
    public void afterConnectionClosed( @NonNull WebSocketSession session, @NonNull CloseStatus status ) throws IOException {
        roomHandler.leaveRoom( session );
    }

}