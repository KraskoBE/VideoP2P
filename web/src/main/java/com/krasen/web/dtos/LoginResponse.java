package com.krasen.web.dtos;

import lombok.*;

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
