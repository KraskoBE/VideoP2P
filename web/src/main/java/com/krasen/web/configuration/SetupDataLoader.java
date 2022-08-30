package com.krasen.web.configuration;

import java.util.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.krasen.web.models.*;
import com.krasen.web.repositories.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = LoggerFactory.getLogger( SetupDataLoader.class );
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public SetupDataLoader( RoleRepository roleRepository, RoomRepository roomRepository ) {
        this.roleRepository = roleRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        initializeRoles();
        clearRoomParticipants();
    }

    private void initializeRoles() {
        Arrays.stream( RoleType.values() ).forEach( this::createRoleIfNotFound );
        logger.info( "Roles initialized" );
    }

    private void createRoleIfNotFound( RoleType name ) {
        if( roleRepository.findByName( name ).isEmpty() ) {
            roleRepository.save( Role.builder().name( name ).build() );
        }
    }

    private void clearRoomParticipants() {
        List<Room> allRooms = this.roomRepository.findAll();
        allRooms.forEach( room -> room.getUsers().clear() );
        this.roomRepository.saveAll( allRooms );
        logger.info( "Room participants cleared" );
    }

}
