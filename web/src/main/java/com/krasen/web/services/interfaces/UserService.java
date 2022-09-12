package com.krasen.web.services.interfaces;

import com.krasen.web.models.User;

import java.util.UUID;

public interface UserService {

    User updatePicture( String imageString, User currentUser );

    User getUserFromSessionIdAndRoomId( String sessionId, UUID roomId );

}
