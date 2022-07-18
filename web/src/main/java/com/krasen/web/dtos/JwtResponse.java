package com.krasen.web.dtos;

import com.krasen.web.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private Long id;
    private String token;
    private String username;
    private String email;
    private List<String> roles;

}
