package com.krasen.web.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

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

    @CreatedDate
    private Date createdOn;

    @Column
    private Boolean publicRoom;

}
