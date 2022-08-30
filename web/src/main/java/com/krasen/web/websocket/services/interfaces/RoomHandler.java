package com.krasen.web.websocket.services.interfaces;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.web.socket.WebSocketSession;

import com.krasen.web.models.Room;

public interface RoomHandler {

    void joinRoom( WebSocketSession session ) throws IOException;

    void leaveRoom( WebSocketSession session ) throws IOException;

    HashMap<String, WebSocketSession> getParticipantsFromRoom( Room room );

    Room getRoomFromSession( WebSocketSession session ) throws IOException;

}
