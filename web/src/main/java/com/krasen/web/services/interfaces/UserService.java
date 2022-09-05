package com.krasen.web.services.interfaces;

import com.krasen.web.models.User;

public interface UserService {

    User updatePicture( String imageString, User currentUser );

}
