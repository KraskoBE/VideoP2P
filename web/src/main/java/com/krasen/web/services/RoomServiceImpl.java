package com.krasen.web.services;

import com.krasen.web.dtos.RoomDTO;
import com.krasen.web.exceptions.GenericException;
import com.krasen.web.models.Room;
import com.krasen.web.models.User;
import com.krasen.web.repositories.RoomRepository;
import com.krasen.web.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl( RoomRepository roomRepository ) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO create( String roomName, User currentUser, Boolean publicRoom ) {
        try {
            return new RoomDTO( roomRepository.save( Room.builder()
                                                             .name( roomName )
                                                             .createdBy( currentUser )
                                                             .createdOn( new Date() )
                                                             .publicRoom( publicRoom )
                                                             .build() ) );
        } catch ( Exception ex ) {
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
        return roomRepository.getAllByPublicRoomTrueOrderByCreatedOnDesc().stream().map( RoomDTO::new ).collect( Collectors.toList() );
    }

    @Override
    public Room getRoomById( UUID id ) {
        return roomRepository.findById( id ).orElseThrow( () -> new GenericException( "Room not found" ) );

    }

    @Override
    public RoomDTO getRoomDtoById( UUID id ) {
        return new RoomDTO( getRoomById( id ) );
    }

}
