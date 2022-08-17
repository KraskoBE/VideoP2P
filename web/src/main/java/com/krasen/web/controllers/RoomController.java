package com.krasen.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.services.interfaces.RoomService;

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
    public ResponseEntity<RoomDTO> create() {
        return ResponseEntity.ok( roomService.create() );
    }

}
