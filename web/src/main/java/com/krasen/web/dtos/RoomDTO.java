package com.krasen.web.dtos;

import java.util.UUID;

import lombok.Data;

import com.krasen.web.models.Room;

@Data
public class RoomDTO {

    private UUID id;
    private String name;
    private Long createdById;

    public RoomDTO( Room room ) {
        this.id = room.getId();
        this.name = room.getName();
        this.createdById = room.getCreatedBy().getId();
    }

}
