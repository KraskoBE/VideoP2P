package com.krasen.web.websocket.services.interfaces;

import org.springframework.web.socket.*;

public interface MessageHandler {

    void handleTextMessage( WebSocketSession session, TextMessage message ) throws Exception;

}
