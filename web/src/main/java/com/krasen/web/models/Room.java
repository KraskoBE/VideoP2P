package com.krasen.web.models;

import java.util.UUID;
import javax.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "rooms" )
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JsonIdentityReference( alwaysAsId = true )
    private User createdBy;

}
