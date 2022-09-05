package com.krasen.web.services;

import com.krasen.web.models.User;
import com.krasen.web.repositories.UserRepository;
import com.krasen.web.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    public User updatePicture( String pictureString, User currentUser ) {
        currentUser.setPictureString( pictureString );
        return userRepository.save( currentUser );
    }

}
