package com.krasen.web.services;

import java.util.*;
import java.util.stream.Collectors;

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
            return new RoomDTO( roomRepository.save( Room.builder()
                                                         .name( roomName )
                                                         .createdBy( currentUser )
                                                         .createdOn( new Date() )
                                                         .build() ) );
        } catch( Exception ex ) {
            throw new GenericException( "Room name already in use" );
        }
    }

    @Override
    public List<RoomDTO> getUserRooms( User currentUser ) {
        return roomRepository.getRoomsByCreatedByUsernameOrderByCreatedOnDesc( currentUser.getUsername() )
                             .stream()
                             .map( RoomDTO::new )
                             .collect( Collectors.toList() );
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.getAllByOrderByCreatedOnDesc().stream().map( RoomDTO::new ).collect( Collectors.toList() );
    }

}
