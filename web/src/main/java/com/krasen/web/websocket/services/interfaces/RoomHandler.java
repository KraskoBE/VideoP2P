package com.krasen.web.websocket.services.interfaces;

import com.krasen.web.models.Room;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.IOException;
import java.util.HashMap;

public interface RoomHandler {

    void joinRoom( StandardWebSocketSession session ) throws IOException;

    void leaveRoom( StandardWebSocketSession session ) throws IOException;

    HashMap<String, StandardWebSocketSession> getParticipantsFromRoom( Room room );

    Room getRoomFromSession( StandardWebSocketSession session ) throws IOException;

}
