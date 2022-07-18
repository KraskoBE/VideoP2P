package com.krasen.web.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private boolean accountNonExpired = false;

    @NotNull
    @Column
    @JsonIgnore
    private boolean accountNonLocked = false;

    @NotNull
    @Column
    @JsonIgnore
    private boolean credentialsNonExpired = false;

    @NotNull
    @Column
    private boolean enabled = true;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "role_id" ) )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ( isNull( getRoles() ) ) {
            return null;
        }
        return getRoles().stream().map( role -> new SimpleGrantedAuthority( role.getName().name() ) ).collect( Collectors.toList() );
    }

}
