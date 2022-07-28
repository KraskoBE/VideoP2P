package com.krasen.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String token;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

}
