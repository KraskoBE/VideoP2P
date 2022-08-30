package com.krasen.web.models;

import javax.persistence.*;

import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "roles" )
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @Enumerated( EnumType.STRING )
    private RoleType name;

}
