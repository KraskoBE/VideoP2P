package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.models.User;

public interface RoomService {

    RoomDTO create( String roomName, User currentUser );

}