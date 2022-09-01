package com.krasen.web.models;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import static java.util.Objects.isNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "users" )
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column( unique = true )
    private String username;

    @NotNull
    @Column( unique = true )
    private String email;

    @NotNull
    @Column
    private String firstName;

    @NotNull
    @Column
    private String lastName;

    @NotNull
    @Column
    @JsonIgnore
    private String password;

    @NotNull
    @Column
    @JsonIgnore
    private boolean accountNonExpired;

    @NotNull
    @Column
    @JsonIgnore
    private boolean accountNonLocked;

    @NotNull
    @Column
    @JsonIgnore
    private boolean credentialsNonExpired;

    @NotNull
    @Column
    private boolean enabled;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn( name = "user_id" ),
                inverseJoinColumns = @JoinColumn( name = "role_id" ) )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if( isNull( getRoles() ) ) {
            return null;
        }
        return getRoles().stream()
                         .map( role -> new SimpleGrantedAuthority( role.getName().name() ) )
                         .collect( Collectors.toList() );
    }

}
