package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.models.Room;
import com.krasen.web.models.User;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomDTO create( String roomName, User currentUser, Boolean publicRoom );

    List<RoomDTO> getUserRooms( User currentUser );

    List<RoomDTO> getAllRooms();

    Room getRoomById( UUID id );

    RoomDTO getRoomDtoById( UUID id );

}