package com.krasen.web.websocket.services.interfaces;

import com.krasen.web.models.Room;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface MessageHandler {

    void handleTextMessage( WebSocketSession session, TextMessage message ) throws Exception;

    void alertForFailedVerification( Room room, String alert ) throws IOException;

}
