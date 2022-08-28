package com.krasen.web.models;

import java.util.UUID;
import javax.persistence.*;

import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @Column( unique = true )
    @Length( min = 6 )
    private String name;

    @ManyToOne
    @JsonIdentityReference( alwaysAsId = true )
    private User createdBy;

}
