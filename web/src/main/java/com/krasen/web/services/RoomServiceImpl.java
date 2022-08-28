package com.krasen.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.*;
import com.krasen.web.repositories.RoomRepository;
import com.krasen.web.services.interfaces.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl( RoomRepository roomRepository ) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO create( String roomName, User currentUser ) {
        try {
            return new RoomDTO( roomRepository.save( Room.builder().name( roomName ).createdBy( currentUser ).build() ) );
        } catch( Exception ex ) {
            throw new GenericException( "Room name already in use" );
        }
    }

}
