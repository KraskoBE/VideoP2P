package com.krasen.web.services.interfaces;

import java.util.*;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.models.User;

public interface RoomService {

    RoomDTO create( String roomName, User currentUser );

    List<RoomDTO> getUserRooms( User currentUser );

    List<RoomDTO> getAllRooms();

    RoomDTO joinRoom( UUID roomId, User currentUser );

}