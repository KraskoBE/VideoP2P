package com.krasen.web.dtos;

import com.krasen.web.models.Room;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class RoomDTO {

    private UUID id;
    private String name;
    private String createdBy;
    private Date createdOn;
    private Boolean publicRoom;

    public RoomDTO( Room room ) {
        this.id = room.getId();
        this.name = room.getName();
        this.createdBy = room.getCreatedBy().getUsername();
        this.createdOn = room.getCreatedOn();
        this.publicRoom = room.getPublicRoom();
    }

}
