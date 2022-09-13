package com.krasen.web.controllers;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.models.User;
import com.krasen.web.services.interfaces.RoomService;
import com.krasen.web.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping( "/api/room" )
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController( RoomService roomService ) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<RoomDTO> create( @RequestParam final String roomName,
                                           @RequestParam final Boolean publicRoom,
                                           @CurrentUser final User currentUser ) {
        return ResponseEntity.ok( roomService.create( roomName, currentUser, publicRoom ) );
    }

    @GetMapping
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok( roomService.getAllRooms() );
    }

    @GetMapping( "/my" )
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<List<RoomDTO>> getUserRooms( @CurrentUser final User currentUser ) {
        return ResponseEntity.ok( roomService.getUserRooms( currentUser ) );
    }

    @GetMapping( "/{id}" )
    @PreAuthorize( "hasRole('USER')" )
    public ResponseEntity<RoomDTO> getRoomById( @PathVariable final UUID id ) {
        return ResponseEntity.ok( roomService.getRoomDtoById( id ) );
    }

}
