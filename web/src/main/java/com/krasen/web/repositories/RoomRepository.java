package com.krasen.web.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krasen.web.models.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> getRoomsByCreatedByUsernameOrderByCreatedOnDesc( String username );

    List<Room> getAllByOrderByCreatedOnDesc();

}
