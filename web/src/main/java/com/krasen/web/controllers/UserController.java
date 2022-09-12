package com.krasen.web.controllers;

import com.krasen.web.models.User;
import com.krasen.web.services.interfaces.UserService;
import com.krasen.web.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping( "/api/user" )
@CrossOrigin( origins = "*" )
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController( UserService userService ) {
        this.userService = userService;
    }

    @GetMapping( "/current" )
    public ResponseEntity<User> getCurrentUser( @CurrentUser final User currentUser ) {
        return ResponseEntity.ok( currentUser );
    }

    @PutMapping( "/picture" )
    public ResponseEntity<User> updatePicture( @RequestBody final String imageString, @CurrentUser final User currentUser ) {
        return ResponseEntity.ok( userService.updatePicture( imageString, currentUser ) );
    }

    @GetMapping( "/user-from-room/{sessionId}/{roomId}" )
    public ResponseEntity<User> getUserFromSessionIdAndRoomId( @PathVariable final String sessionId, @PathVariable final UUID roomId ) {
        return ResponseEntity.ok( userService.getUserFromSessionIdAndRoomId( sessionId, roomId ) );
    }

}
